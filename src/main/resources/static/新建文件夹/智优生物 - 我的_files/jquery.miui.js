/**
 * @description my UI
 * @required jQuery
 * @author Yoki
 */

;
(function() {

  var _miui_dialog_index = 0; // 弹出窗口id

  /**
   * 弹出窗口
   */
  $.dialog = function(options) {

    if (typeof (options) == 'string') {
      options = {
        content : options,
        skin : 'message',
        timeout : 2,
        btn : [],
        btnCancel : null
      };
      if (arguments.length > 1) {
        options.timeout = arguments[1];
      }
    }
    
    options = $.extend({}, $.dialog.defaults, options || {});
    if (!options.content && (!options.btn || options.btn.length == 0)) {
      alert('[缺少参数]content 和 btn 不能为空!');
      return;
    }
    
    if(options.skin == 'message' && typeof(options.shade) == 'undefined') {
      options.shade = false;
      options.shadeClose = false;
    }
    if(options.skin == 'footer' && typeof(options.shade) == 'undefined') {
      options.shade = true;
      options.shadeClose = true;
    }
    if(options.skin == 'center' && typeof(options.shade) == 'undefined') {
      options.shade = true;
      options.shadeClose = false;
    }
    
    options.id = 'miui_dialog_' + (_miui_dialog_index++);
    var html = '<aside id="' + options.id + '" class="miui-dialog" data-index="' + _miui_dialog_index++ + '">';
    if(options.shade){
      html += '<div class="miui-dialog-shade"></div>';
    }
    html += '<div class="miui-dialog-wrap">'
      +     '<div class="miui-dialog-inner">';
    var skinClass = '';
    if(options.skin == 'footer') {
      skinClass = ' miui-dialog-footer miui-animation-up';
    } else if(options.skin == 'center') {
      skinClass = ' miui-dialog-center miui-animation-scale';
    } else if(options.skin == 'message') {
      skinClass = ' miui-dialog-message miui-animation-up';
    }
    var style = (options.style ? ' style="' + options.style + '"' : '')
    html +=   '<div class="miui-dialog-content' + skinClass + '"' + style + '>';
    if(options.content){
      html +=   '<div class="miui-dialog-title">' + options.content + '</div>';
    }
    html +=     '<div class="miui-dialog-buttons">';
    $.each(options.btn, function(index){
      html +=     '<div class="miui-dialog-button" data-index="' + (index + 1) + '">' + options.btn[index] + '</div>';
    });
    if(options.btnCancel){
      html +=     '<div class="miui-dialog-button miui-dialog-button-cancle" data-index="0">' + options.btnCancel + '</div>';
    }
    html +=     '</div>'
      +       '</div>'
      +     '</div>'
      +   '</div>'
      + '</aside>';
    var $dialog = $(html);
    $('body').css({'overflow':'hidden'}).append($dialog);
    $dialogContent = $dialog.find('.miui-dialog-content');
    var onClose = function(){
      $('body').css({'overflow':'auto'});
      $dialog.remove();
    };
    
    if (options.timeout != 0) {
      setTimeout(function() {
        onClose();
      }, options.timeout * 1000);
    }
    if(options.shadeClose) {
      $dialog.find('.miui-dialog-shade').click(function(){
        onClose();
      });
    }
    $dialog.find('.miui-dialog-button-cancle').click(function(){
      onClose();
    });
    $dialog.find('.miui-dialog-button').click(function(){
      var index = $(this).attr('data-index');
      if ($.isFunction(options.callback)) {
        if(options.callback.call($dialog, index)) {
          onClose();
        }
      } else {
        onClose();
      }
    });
    return $dialog;
  };

  $.dialog.defaults = $.extend({}, {
    content : '',
    skin : 'center', //center,footer,message
    style : '',
    timeout : 0,
    btn : [ '确定' ],
    btnCancel : '取消',
    callback : function(index) {
      //alert(index);
      return true;
    }
  });

  /**
   * 信息提示
   */
  $.message = function(options) {
    if (typeof (options) == 'string') {
      options = {
        content : options
      };
      if (arguments.length > 1) {
        options.type = arguments[1];
      }
      if (arguments.length > 2) {
        options.timeout = arguments[2];
      }
    }
    options = $.extend({}, $.message.defaults, options || {});
    if (options.content == null) {
      options.content = '';
    }

    var html = '<div class="header-message ';
    if (options.type == 'success') {
      html += 'header-message-success';
    } else if (options.type == 'error') {
      html += 'header-message-error';
    } else {
      html += 'header-message-info';
    }
    html += '">';
    html += '<i class="message-icon"></i>';
    if (options.closable){
      html += '<i class="message-close fa fa-times"></i>';
    }
    html += '<div class="message-text">' + options.content + '</div>';
    html += '</div>';
    var $message = $(html);
    
    if ($.isFunction(options.callback)) {
      $message.click(function(){
        if (options.callback.call($message, window.jQuery || window.Zepto) != false) {
          $message.slideUp(300, function(){
            $message.remove();
          });
        }
      });
    }
    
    var onClose = function() {
      $message.slideUp(300, function(){
        $message.remove();
      });
    };
    
    if (options.closable){
      var $messageClose = $message.find('.message-close');
      $messageClose.click(onClose);
    }
    
    $message.appendTo('body').slideDown(300);
    if (options.timeout) {
      setTimeout(function() {
        $message.slideUp(300, onClose);
      }, options.timeout * 1000);
    }
    return $message;
  };

  $.message.defaults = $.extend({}, {
    type : 'info', // info, success, error
    content : '',
    closable : true,
    timeout : 0,
    callback : function(message) {
    }
  });
  
  /**
   * Image view
   */
  $.imageview = function(options) {
    if(typeof(options) == 'string') {
      options = {url: options};
    } else if(typeof(options) == 'Array') {
      options = {url: options};
    }
    options = $.extend({}, $.imageview.defaults, options || {});
    if (options.url == null || options.url.length == 0) {
      alert('缺少参数[图片URL]!');
      return;
    }
    if(typeof(options.url) == 'string') {
      var arr = [];
      arr.push(options.url);
      options.url = arr;
    }
    
    var ths = this;
    var str = '<aside class="miui-imageview">'
          +   '<a href="javascript:;" class="btn-close button-right"><i class="fa fa-times-circle"></i></a>';
    if(options.title){
      str +=  '<header class="header">'
          +   '<h2>' + options.title + '</h2>'
          +   '</header>';
    }
    str +=    '<ul></ul>'
          +   '<div class="loading"></div>';
    if(options.url.length > 1) {
      str +=  '<div class="view-control control-left"></div>'
          +   '<div class="view-control control-right"></div>'
          +   '<div class="view-index"><em>' + (options.current + 1) + '</em> / ' + options.url.length + '</div>';
    }
    str +=    '</aside>';
    $imageview = $(str);
    $('body').css({'overflow': 'hidden'}).append($imageview);
    $imageList = $imageview.find('ul');
    $loading = $imageview.find('.loading');
    $index = $imageview.find('.view-index');
    
    var onClose = function(){
      $imageview.animate({opacity: 0}, 300, function(){
        $imageview.remove();
      })
      $('body').css({'overflow': 'auto'});
    };
    if(options.shadeClose){
      $imageList.click(function(){
        //onClose();
        $index.stop(true).slideToggle(300);
        $imageview.find('.view-control').stop(true).toggle(300);
        $imageview.find('.header').stop(true).slideToggle(300);
      });
    }
    $imageview.find('.btn-close').click(onClose);
    
    var wrapWidth = $(window).width(), wrapHeight = $(window).height() - (options.title ? 48 : 0);
    var listWidth = $imageList.width();
    
    
    ths.current = options.current;
    ths.size = options.url.length;
    
    $.each(options.url, function(n, url){
      $image = $('<li' + (options.current == n ? ' class="current"' : '') + '><div><img style="display:none" src="' + url + '"></div></li>');
      $image.width(wrapWidth).appendTo($imageList);
      $image.find('img').bind('load', function(){
        var $this = $(this);
        w = $this.width();
        h = $this.height();
        if (w / h >= wrapWidth / wrapHeight) {
          if (w > wrapWidth) {
            h = (h * wrapWidth) / w;
            w = wrapWidth;
          }
        } else {
          if (h > wrapHeight) {
            w = (w * wrapHeight) / h;
            h = wrapHeight;
          }
        }
        $this.width(w).height(h).show();
        $loading.remove();
      });
    });
    
    if(ths.current != 0) {
      $imageList.css({'left' : - wrapWidth * ths.current });
    }
    
    $imageview.find('.control-left').bind('click', function(){
      prev();
    });
    $imageview.find('.control-right').bind('click', function(){
      next();
    });
    
    var x1, y1, x2, y2, x3, y3;
    $imageList.on('touchstart', function(event) {
      var touch1 = event.originalEvent.targetTouches[0];
      x1 = touch1.pageX;
      y1 = touch1.pageY;
    }).on('touchmove', function(event) {
      var touch2 = event.originalEvent.targetTouches[0];
      x2 = touch2.pageX;
      y2 = touch2.pageY;
      if (Math.abs(y2 - y1) > 0) {
        event.preventDefault();
      }
    }).on('touchend', function(e) {
      //var touch3 = e.originalEvent.changedTouches[0];
      if(x2 - x1 > 20) {
        prev();
      } else if(x2 - x1 < -20){
        next();
      }
    });
    
    var next = function() {
      if(ths.current == ths.size - 1) {
        return;
      }
      $imageList.stop(true).animate({'left' : - wrapWidth * (++ths.current)}, 300, function(){
        $index.find('em').text(ths.current + 1);
        $imageList.find('li').removeClass('current').eq(ths.current).addClass('current');
      });
    };
    var prev = function() {
      if(ths.current == 0) {
        return;
      }
      $imageList.stop(true).animate({'left' : - wrapWidth * (--ths.current)}, 300, function(){
        $index.find('em').text(ths.current + 1);
        $imageList.find('li').removeClass('current').eq(ths.current).addClass('current');
      });
    }
  }
  
  $.imageview.defaults = $.extend({}, {
    url : '',
    title : '',
    current : 0,
    shadeClose : true
  });

  
  $.fn.scrollableNav = function() {
    var $this = $(this);
    var $cur = $('<li class="cur-line">').appendTo($this.find('ul'));
    var itemWidth = $this.find('li.current').width();
    $cur.width(itemWidth);
    var wrapWidth = $this.width();
    var listWidth = $this.find('ul').width();
    //console.info('itemWidth : ' + itemWidth);
    //console.info('wrapWidth : ' + wrapWidth);
    //console.info('listWidth : ' + listWidth);
    $this.find('li').on('click', function() {
      var $li = $(this);
      itemWidth = $li.outerWidth();
      var leftHalf = (wrapWidth - itemWidth) / 2;
      var posLeft = parseInt($li.position().left);
      var scrollLeft;
      if (posLeft <= leftHalf) {
        scrollLeft = 0;
      } else if (leftHalf - posLeft <= wrapWidth - listWidth) {
        scrollLeft = wrapWidth - listWidth;
      } else {
        scrollLeft = leftHalf - posLeft;
      }
      $this.find('ul').animate({
        'left' : scrollLeft
      }, 300, function(){
        $li.addClass('current').siblings().removeClass('current');
      });
      $cur.stop(true).animate({
        left : $li.position().left
      }, 300).animate({
        width : itemWidth
      }, 100);
    });
    
    $this.find('li.current').click();
    
    var x1, x2, y1, y2;
    $this.find('ul').on('touchstart', function(e) {
      var touch1 = e.originalEvent.targetTouches[0];
      x1 = touch1.pageX;
      y1 = touch1.pageY;
      cssLeft = parseInt($(this).css('left'));
    }).on('touchmove', function(e) {
      var touch2 = e.originalEvent.targetTouches[0];
      x2 = touch2.pageX;
      y2 = touch2.pageY;
      if (cssLeft + x2 - x1 >= 0) {
        $(this).css('left', 0);
      } else if (cssLeft + x2 - x1 <= wrapWidth - listWidth) {
        $(this).css('left', wrapWidth - listWidth);
      } else {
        $(this).css('left', cssLeft + x2 - x1);
      }
      if (Math.abs(y2 - y1) > 0) {
        e.preventDefault();
      }
    });
  };
  
  $.fn.scrollableNav.defaults = $.extend({}, {
    scrollable : true
  });

  $.fn.tabs = function(tabContent, eventName) {
  	eventName = !eventName ? 'click' : eventName;
    var $tabs = $(this);
    var $tabContent = $(tabContent);
    $tabs.each(function() {
      var $this = $(this);
      var index = $tabs.index($this);
      if ($this.hasClass('current')) {
        $tabContent.eq(index).show();
      } else {
        $tabContent.eq(index).hide();
      }
    });
    $tabs.bind(eventName, function() {
      var $this = $(this);
      var index = $tabs.index($this);
      $this.addClass('current').siblings().removeClass('current');
      $tabContent.hide().eq(index).show();
    });
    var interval;
    if (interval) {
    	var showPanel = function(index) {
      	var count = $tabs.size();
        if (index < 0) {
          index = count - 1;
        } else if (index >= count) {
          index = 0;
        }
        $tabs.removeClass('current').eq(index).addClass('current');
        $tabContent.hide().eq(index).show();
      };
      
    	var index = 0;
    	var autoInterval = setInterval(function() {
        showPanel(index++);
      }, interval);
    }
  };

  $.fn.timer = function(options) {
    if (typeof (options) == 'string') {
      options = {
        beginText : options
      };
      if (arguments.length > 1) {
        options.endText = arguments[1];
      }
    }
    options = $.extend({}, $.fn.timer.defaults, options || {});
    $(this).each(function() {
      var $this = $(this);
      var beginTime = $this.attr('data-begin-time');
      if(beginTime){
        beginTime = strToDate(beginTime);
      }
      var endTime = strToDate($this.attr('data-end-time'));
      var nowTime = new Date().getTime();
      var timerInterval = function() {
        nowTime = new Date().getTime();
        var bal = endTime - nowTime;
        
        bal_day = Math.floor(bal / (60*60*24*1000));
        bal_hour = Math.floor((bal - (bal_day*60*60*24*1000)) / (60*60*1000));
        bal_minute = Math.floor((bal - (bal_day*60*60*24*1000) - (bal_hour*60*60*1000)) / (60*1000));
        bal_second = Math.floor((bal - (bal_day*60*60*24*1000) - (bal_hour*60*60*1000) - (bal_minute*60*1000)) / 1000);
        var html = '';
        if(bal_day > 0) {
          html += bal_day + '<em>' + options.units[0] + '</em>';
        }
        html += (bal_hour < 10 ? '0' + bal_hour : bal_hour) + '<em>' + options.units[1] + '</em>';
        html += (bal_minute < 10 ? '0' + bal_minute : bal_minute) + '<em>' + options.units[2] + '</em>';
        html += (bal_second < 10 ? '0' + bal_second : bal_second) + '<em>' + options.units[3] + '</em>';
        
        $this.html(html);
      };
      if (beginTime && beginTime > nowTime) { // 未开始
        $this.html(options.beginText);
      } else if (endTime > nowTime) { // 进行中
        timerInterval();
        setInterval(timerInterval, 1000);
      } else if (endTime < nowTime) { // 已结束
        $this.html(options.endText);
        clearInterval(timerInterval);
      }
    });

    // 字符串转换成日期对象函数yyyy-MM-dd HH:mm:ss
    function strToDate(dateStr) {
      var arr = dateStr.split(' ');
      arr[0] = arr[0].split('-');
      arr[1] = arr[1].split(':');
      var theDate = new Date(arr[0][0], (arr[0][1] - 1), arr[0][2], arr[1][0], arr[1][1], arr[1][2]);
      return theDate.getTime();
    }
  };
  
  $.fn.timer.defaults = $.extend({}, {
    beginTime : null,
    endTime : null,
  	beginText : '未开始',
    endText : '已结束',
    units: ['天', '时', '分', '秒']
  });

})(jQuery);
