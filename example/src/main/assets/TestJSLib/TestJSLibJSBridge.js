function connectWebViewJavascriptBridge(callback) {
    if (window.AndroidJavascriptBridge && AndroidJavascriptBridge.inited) {
         console.log('JS connectWebViewJavascriptBridge 初始化了');
         callback(AndroidJavascriptBridge)
    } else {
       console.log('JS connectWebViewJavascriptBridge 添加监听');
     document.addEventListener(
            'AndroidJavascriptBridgeReady'
            , function() {
                callback(AndroidJavascriptBridge)
            },
            false
        );
    }
}

connectWebViewJavascriptBridge(function(bridge) {
    console.log('JS connectWebViewJavascriptBridge ++++++++++');
    bridge.init(function(){
           console.log('bridge init');
    });

    bridge.registerHandler("js_bridge_channel", function(data, responseCallback) {
        document.getElementById("show").innerHTML = ("data from Java: = " + data + Date.now());
        if (responseCallback) {
            var responseData = "Javascript Says Right back aka!";
            responseCallback(responseData);
        }
    });
})