function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge && WebViewJavascriptBridge.inited) {
         console.log('JS connectWebViewJavascriptBridge 初始化了');
         callback(WebViewJavascriptBridge)
    } else {
       console.log('JS connectWebViewJavascriptBridge 添加监听');
     document.addEventListener(
            'WebViewJavascriptBridgeReady'
            , function() {
                callback(WebViewJavascriptBridge)
            },
            false
        );
    }
}

connectWebViewJavascriptBridge(function(bridge) {
    console.log('JS connectWebViewJavascriptBridge ++++++++++');
    bridge.init(function(message, responseCallback) {
        console.log('JS got a message', message);
        var data = {
            'Javascript Responds': '测试中文!'
        };

        if (responseCallback) {
            console.log('JS responding with', data);
            responseCallback(data);
        }
    });

    bridge.registerHandler("functionInJs", function(data, responseCallback) {
        document.getElementById("show").innerHTML = ("data from Java: = " + data + Date.now());
        if (responseCallback) {
            var responseData = "Javascript Says Right back aka!";
            responseCallback(responseData);
        }
    });
})