;
(function() {
  'use strict';
  var imageupload = function(container, options) {
    /** extend from defaults to options */
    options = options || {};
    var originalOptions = {};
    for (var option in options) {
      if (typeof options[option] === 'object' && options[option] !== null && !(options[option].nodeType || options[option] === window || options[option] === document || (typeof Dom7 !== 'undefined' && options[option] instanceof Dom7) || (typeof jQuery !== 'undefined' && options[option] instanceof jQuery))) {
        originalOptions[option] = {};
        for (var deepOption in options[option]) {
          originalOptions[option][deepOption] = options[option][deepOption];
        }
      } else {
        originalOptions[option] = options[option];
      }
    }
    var defaults = this.defaults;
    for (var def in defaults) {
      if (typeof options[def] === 'undefined') {
        options[def] = defaults[def];
      } else if (typeof options[def] === 'object') {
        for (var deepDef in defaults[def]) {
          if (typeof options[def][deepDef] === 'undefined') {
            options[def][deepDef] = defaults[def][deepDef];
          }
        }
      }
    }
    
    container.style.width = options.width + 'px';
    container.style.height = options.height + 'px';
    
    var ths = this;
    var eleState = container.getElementsByClassName('state')[0];
    if(!eleState){
      var eleState = document.createElement('em');
      container.appendChild(eleState);
    }
    ths.eleState = eleState;
    
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
      // weixin
    }
    
    ths.stopLoading = function(){
      if(options.isMultipart) {
        ths.eleState.className = 'state state-add';
      } else {
        container.removeChild(ths.eleState);
      }
    };
    
    var inputFile = container.getElementsByTagName('input')[1];
    inputFile.onchange = function(evt) {
      var file = inputFile.files[0];
      ths.eleState.className = 'state state-loading';
      
      console.info('Origin File type : ' + file.type);
      console.info('Origin File size : ' + file.size + 'Byte');
      
      if(options.fileTypes.indexOf(file.type) == -1) {
        ths.showError('您选择的文件类型不合法，请重新选择。');
        ths.stopLoading();
        return;
      }
      if(options.quality < 1){
        ths.compressFile(file)
      } else {
        ths.fileData = file;
        ths.startUploadFile();
      }
    };
    
    /** compress method */
    ths.compressFile = function(file) {
      if (typeof FileReader === 'undefined') {
        console('Your browser does not support FileReader...');
        ths.fileData = file;
        startUploadFile();
        return;
      }
      var image = new Image();
      var reader = new FileReader();
      reader.onload = function() {
        var url = reader.result;
        image.src = url;
      };
      var MaxSide = 1280;
      image.onload = function() {
        var sw = image.naturalWidth,
            sh = image.naturalHeight;
        console.info('Origin File pixel : ' + sw + ' x ' + sh);
        var w = sw, h = sh;
        if(w > h) {
          if(w > MaxSide) {
            h = Math.round(MaxSide * h / w);
            w = MaxSide;
          }
        } else {
          if(h > MaxSide) {
            w = Math.round(MaxSide * w / h);
            h = MaxSide;
          }
        }
        console.info('Compressed File pixel : ' + w + ' x ' + h);
        try {
          var canvas = document.createElement('canvas');
          canvas.width = w;
          canvas.height = h;
          var ctx = canvas.getContext('2d');
          ctx.drawImage(image, 0, 0, sw, sh, 0, 0, w, h);
          var format = 'image/jpeg';
          var base64 = canvas.toDataURL(format, options.quality);
          var code = window.atob(base64.split(',')[1]);
          if(code.length == 0) {
            throw new Error('该浏览器不支持Canvas绘图.');
          }
          var aBuffer = new window.ArrayBuffer(code.length);
          var uBuffer = new window.Uint8Array(aBuffer);
          for(var i = 0; i < code.length; i++){
            uBuffer[i] = code.charCodeAt(i) & 0xff ;
          }
          ths.fileData = new Blob([uBuffer], {
            type: format
          });
          console.info('Compressed File size : ' + ths.fileData.size + 'Byte');
        } catch (e) {
          ths.fileData = file;
          console.info('Canvas compress failed...' + e.message);
        }
        ths.startUploadFile();
      };
      reader.readAsDataURL(file);
    }
    
    /** real upload method */
    ths.startUploadFile = function() {
      if (options.maxFileSize && ths.fileData.size > Utils.sizeToBytes(options.maxFileSize)) {
        ths.showError('您选择的文件超过' + options.maxFileSize + '，请重新选择。');
        ths.stopLoading();
      }
      
      var formData = new FormData();
      formData.append('file', ths.fileData);
      formData.append('width', options.width *　(options.retain ? 2 : 1));
      formData.append('height', options.height * (options.retain ? 2 : 1));
      
      var oXHR = new XMLHttpRequest();
      oXHR.addEventListener('load', ths.callbacks.uploadSuccess, false);
      oXHR.addEventListener('error', ths.callbacks.uploadError, false);
      oXHR.addEventListener('progress', ths.callbacks.uploadProgress, false);
      oXHR.addEventListener('abort', ths.callbacks.uploadAbort, false);

      if(options.xhrFields){
        for (var e in options.xhrFields){
          oXHR[e] = options.xhrFields[e];
        }
      }

      oXHR.open('POST', options.url);
      oXHR.send(formData);
    };
    ths.showError = function(message) {
      if((window.jQuery || window.Zepto) && $.message){
        //$.message(message, 'error');
        $.dialog({
          content : message,
          btn : [ '我知道了' ],
          btnCancel : null,
          shadeClose : true
        });
      } else {
        alert(message);
      }
    };
    ths.callbacks = {
        uploadProgress : function(e) {
          if (options.progress) {
            options.progress.call(container, e);
          }
        },
        uploadAbort : function(e) {
          if (options.abort) {
            options.abort.call(container, e);
          }
        },
        uploadError : function(e) {
          if(e.target.status == 200) {
            var result = JSON.parse(e.target.responseText);
            alert('[' + result.code + ']' + result.message + '');
          } else {
            alert('[' + e.target.status + ']' + '图片上传失败，请重试');
          }
          ths.stopLoading();
          if (options.error) {
            options.error.call(container, e);
          }
        },
        uploadSuccess: function(e) {
          if(e.target.status != 200){
            ths.callbacks.uploadError(e);
            return;
          }
          var result = JSON.parse(e.target.responseText);
          if(result.code != 0) {
            ths.callbacks.uploadError(e);
            return;
          }
          var resultData = result.data;
          // alert(result);
          
          if(options.isMultipart) {
            ths.stopLoading();
            //TODO 多图上传
          } else {
            var inputHidden = container.getElementsByTagName('input')[0];
            if(inputHidden){
              inputHidden.value = resultData;
            }
            var imageObj = container.getElementsByTagName('img')[0];
            if(imageObj) {
              imageObj.src = imageNginxUrl+resultData;
              imageObj.onload = function(){
                ths.stopLoading();
              };
            }
          }
          if(options.success) {
            options.success.call(container, resultData);
          }
        }
    };
    var Utils = {
       sizeToBytes : function(size) {
         if (!size) {
           return '0';
         }
         var units = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ];
         for ( var i in units) {
           var regexp = new RegExp('(\\d+)' + units[i] + '$');
           if (regexp.test(size)) {
             var num = size.replace(regexp, '$1');
             return num * Math.pow(1024, i);
           }
         }
         return 0;
       },
       bytesToSize : function(bytes) {
         if (bytes === 0) {
           return '0 B';
         }
         var k = 1024,
         units = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ],
         i = Math.floor(Math.log(bytes) / Math.log(k));

         return (bytes / Math.pow(k, i)).toPrecision(3) + '' + units[i];
       }
    };
  };

  window.imageupload = imageupload;
  
  window.imageupload.prototype.defaults = {
    width : 100,
    height : 100,
    retain : true,
    quality : 0.6,
    fileTypes : ['image/bmp', 'image/gif', 'image/jpeg', 'image/png'],
    maxFileSize : '10MB',
    isMultipart : false,
    success : function(result){
      
    },
    error :function() {
      
    },
    progress : function() {
      
    },
    abort : function() {
      
    }
  };

  if (window.jQuery || window.Zepto) {
    (function($) {
      $.fn.imageupload = function(options) {
        return this.each(function() {
          $(this).data('imageupload', new imageupload($(this)[0], options));
        });
      };
      $.fn.imageupload.setDefaults = function(defaults) {
        window.imageupload.prototype.defaults = $.extend({}, window.imageupload.prototype.defaults, defaults);
      };
    })(window.jQuery || window.Zepto)
  }

}());