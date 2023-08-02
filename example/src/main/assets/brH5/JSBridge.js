console.log(navigator.userAgent);
function isIos() {
    return /(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent);
}
function isAndroid() {
    return /(Android)/i.test(navigator.userAgent);
}
if (isIos()) {
    function setupWebViewJavascriptBridge(callback) {
        if (window.WebViewJavascriptBridge) { return callback(WebViewJavascriptBridge); }
        if (window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
        window.WVJBCallbacks = [callback];
        var WVJBIframe = document.createElement('iframe');
        WVJBIframe.style.display = 'none';
        WVJBIframe.src = 'https://__bridge_loaded__';
        document.documentElement.appendChild(WVJBIframe);
        setTimeout(function () { document.documentElement.removeChild(WVJBIframe) }, 0)
    }
    setupWebViewJavascriptBridge(function (bridge) {
        bridge.registerHandler('js_bridge_channel', function (data, responseCallback) {
            data = JSON.parse(data);
            const { message } = data;
            switch (message) {
                case "refresh":
                    const url = data.data.url
                    if (url) {
                        window.location.href = url;
                        window.location.reload();
                    }
                    break;
            }
            responseCallback(data);
        })
    });
} else if (isAndroid()) {
    function setupWebViewJavascriptBridge(callback) {
        if (window.AndroidJSBridge && AndroidJSBridge.inited) {
            console.log('JS connectWebViewJavascriptBridge 监听 callback');
            return callback(AndroidJSBridge);
        } else {
            console.log('JS connectWebViewJavascriptBridge 添加监听');
            document.addEventListener(
                   'AndroidJSBridgeReady'
                   , function() {
                       callback(AndroidJSBridge)
                   },
                   false
               );
        }
    }
    setupWebViewJavascriptBridge(function (bridge) {
        console.log('JS connectWebViewJavascriptBridge init++++++++++++');
        bridge.init(function(message, responseCallback) {
            console.log('JS got a message init ', message);
            var data = {
                'Javascript Responds': '测试中文!'
            };
    
            if (responseCallback) {
                console.log('JS responding with', data);
                responseCallback(data);
            }
        });

        bridge.registerHandler('js_bridge_channel', function (data, responseCallback) {
            data = JSON.parse(data);
            const { message } = data;
            switch (message) {
                case "refresh":
                    const url = data.data.url
                    if (url) {
                        window.location.href = url;
                        window.location.reload();
                    }
                    break;
            }
            responseCallback(data);
        })
     });
} else {
    console.log('isPC');
}
