/**
 * 全局静态属性配置文件
 */
const config = {
    langCode:'lang',                                            //默认语言接口参数名
    lang: 'zh_CN',                                              //语言值
    apiPath:'http://127.0.0.1:10050',
    webSocketPath:"ws://127.0.0.1:10050/websocket/",
    // apiPath:'http://120.78.136.100:10050',
    // webSocketPath:"ws://120.78.136.100:10050/websocket/",
    Authorization:"Authorization",
    noLoginUrl:"/api/open/",
    jwtUrl:"/api/jwt/",
    imgUrl:'../../../static/img/upload/',
    loginUrl:"/api/open/user/login",
	HearderParamClubId:"clubId",//头部携带的clubId
}
export default config;
