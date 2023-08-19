function connectWebViewJavascriptBridge(callback) {
    if (window.JSBridge && JSBridge.inited) {
         console.log('JS connectWebViewJavascriptBridge 初始化了');
         callback(JSBridge)
    } else {
       console.log('JS connectWebViewJavascriptBridge 添加监听');
     document.addEventListener(
            'JSBridgeReady'
            , function() {
                callback(JSBridge)
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
})