/*******************************************************************************
 * @description Utils
 * @author http://www.1024ke.com
 * @copyright Copyright © 2014 1024ke.com All Rights Reserved.
 ********************************************************************************
 */

sys = {
  base : '',
  currencySign : '￥',
  currencyUnit : '元',
  currencyScale : '2',
  roundType : "HALFUP"
};

// 解决IE6不缓存背景图片问题
if (!window.XMLHttpRequest) {
  document.execCommand("BackgroundImageCache", false, true);
}

function isWeixin() {
  var ua = navigator.userAgent.toLowerCase();
  if (ua.match(/MicroMessenger/i) == "micromessenger") {
    return true;
  } else {
    return false;
  }
}

function escapeHtml(content) {
  var reg, escapeMap = {
    '&' : '&amp;',
    '<' : '&lt;',
    '>' : '&gt;',
    '"' : '&quot;',
    "'" : '&#x27;',
    '`' : '&#x60;'
  };
  reg = '(?:';
  for ( var key in escapeMap) {
    reg += key + '|';
  }
  reg = reg.slice(0, -1);
  reg += ')';
  reg = RegExp(reg, 'g');
  content = content.replace(reg, function(m) { // escape html
    return escapeMap[m] || m;
  }).replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, "$1<br>$2");

  return content;
}

// 下面这个函数用来转换数组到json格式
function arrayToJson(o) {
  var r = [];
  if (typeof o == "string")
    return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
  if (typeof o == "object") {
    if (!o.sort) {
      for ( var i in o)
        r.push(i + ":" + arrayToJson(o[i]));
      if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
        r.push("toString:" + o.toString.toString());
      }
      r = "{" + r.join() + "}";
    } else {
      for (var i = 0; i < o.length; i++) {
        r.push(arrayToJson(o[i]));
      }
      r = "[" + r.join() + "]";
    }
    return r;
  }
  return o.toString();
}

// 添加收藏夹
function addFavorite(url, title) {
  try {
    window.external.addFavorite(url, title);
  } catch (e) {
    try {
      window.sidebar.addPanel(title, url, "");
    } catch (e) {
      alert('请按 [Ctrl + D] 收藏本页面.');
    }
  }
}

// html字符串转义
function htmlEscape(htmlString) {
  htmlString = htmlString.replace(/&/g, '&amp;');
  htmlString = htmlString.replace(/</g, '&lt;');
  htmlString = htmlString.replace(/>/g, '&gt;');
  htmlString = htmlString.replace(/'/g, '&acute;');
  htmlString = htmlString.replace(/"/g, '&quot;');
  htmlString = htmlString.replace(/\|/g, '&brvbar;');
  return htmlString;
}

//获取参数
function getParameter(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return decodeURIComponent(r[2]);
  } else {
    return null;
  }
}

/**
 * [Cookie] Sets value in a cookie
 */
function setCookie(cookieName, cookieValue, path, expires, domain, secure) {
  if (typeof (expires) == 'undefined') {
    expires = new Date();
    expires.setTime(expires.getTime() + 1000 * 60 * 60 * 24 * 7);
  }
  document.cookie = cookieName + '=' + encodeURIComponent(cookieValue) + (expires ? '; expires=' + expires.toGMTString() : '') + ('; path=' + (path ? path : '/'))
      + ('; domain=' + (domain ? domain : document.domain)) + (secure ? '; secure' : '');
}

/**
 * [Cookie] Gets a value from a cookie
 */
function getCookie(cookieName) {
  var cookieValue = null;
  var posName = document.cookie.indexOf(cookieName + '=');
  if (posName != -1) {
    var posValue = posName + (cookieName + '=').length;
    var endPos = document.cookie.indexOf(';', posValue);
    if (endPos != -1)
      cookieValue = decodeURIComponent(document.cookie.substring(posValue, endPos));
    else
      cookieValue = decodeURIComponent(document.cookie.substring(posValue));
  }
  return cookieValue;
}

/**
 * [Cookie] Clears cookies
 */
function clearCookie(cookieName) {
  var now = new Date();
  var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);
  this.setCookie(cookieName, 'null', yesterday);
}

//浮点数加法运算
function floatAdd(arg1, arg2) {
  var r1, r2, m;
  try {
    r1 = arg1.toString().split(".")[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split(".")[1].length;
  } catch (e) {
    r2 = 0;
  }
  m = Math.pow(10, Math.max(r1, r2));
  return (arg1 * m + arg2 * m) / m;
}

// 浮点数减法运算
function floatSub(arg1, arg2) {
  var r1, r2, m, n;
  try {
    r1 = arg1.toString().split(".")[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split(".")[1].length;
  } catch (e) {
    r2 = 0;
  }
  m = Math.pow(10, Math.max(r1, r2));
  n = (r1 >= r2) ? r1 : r2;
  return ((arg1 * m - arg2 * m) / m).toFixed(n);
}

// 浮点数乘法运算
function floatMul(arg1, arg2) {
  var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
  try {
    m += s1.split(".")[1].length;
  } catch (e) {
  }
  try {
    m += s2.split(".")[1].length;
  } catch (e) {
  }
  return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
}

// 浮点数除法运算
function floatDiv(arg1, arg2) {
  var t1 = 0, t2 = 0, r1, r2;
  try {
    t1 = arg1.toString().split(".")[1].length;
  } catch (e) {
  }
  try {
    t2 = arg2.toString().split(".")[1].length;
  } catch (e) {
  }
  with (Math) {
    r1 = Number(arg1.toString().replace(".", ""));
    r2 = Number(arg2.toString().replace(".", ""));
    return (r1 / r2) * pow(10, t2 - t1);
  }
}

// 设置数值精度
function setScale(value, scale, roundType) {
  if (!roundType) {
    roundType = "HALFUP";
  }
  if (roundType.toLowerCase() == "HALFUP") {
    return (Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale)).toFixed(scale);
  } else if (roundType.toLowerCase() == "UP") {
    return (Math.ceil(value * Math.pow(10, scale)) / Math.pow(10, scale)).toFixed(scale);
  } else { // DOWN
    return (Math.floor(value * Math.pow(10, scale)) / Math.pow(10, scale)).toFixed(scale);
  }
}

// 货币格式化
function formatCurrency(price, withSign, withUnit) {
  price = setScale(price, sys.currencyScale, sys.roundType);
  return (withSign ? sys.currencySign : '') + price + (withUnit ? sys.currencyUnit : '');
}
