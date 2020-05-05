/**
 * 公共JS类
 */
var ykh = ykh || {}
import Vue from 'vue' //vue核心库
import configParams from './config'//配置文件库

import { MessageBox  } from 'element-ui';
// import 'element-ui/lib/theme-default/index.css'


//MD5签名
var MD5 = require('md5.js')
/**
 * 将MD5进行MD5签名，注册，登录，重设密码，解锁等所有场景不直接传递密码。
 * pwd: 用户输入的密码。
 * sPwd: 后台获取的每个用户加密内容。
 */
ykh.password = function (pwd, sPwd) {
  return new MD5().update(pwd + sPwd).digest('hex')
},

/**
 * 日期转换【yyyy-mm-dd】
 */
ykh.yDate = function (date) {
    var date = new Date(Date.parse(new Date(date)));
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();

    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }

    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
},
// //全局的静态属性  s.v.'key'
ykh.v = configParams;




/******************************************************************************** axios 拦截器和请求 ********************************************************************************/
import axios from 'axios' //ajax请求的 内容
//取消请求
let pending = []
let CancelToken = axios.CancelToken
let removePending = (config, f) => {
    let flagUrl = config.url + '&' + config.method + '&' + Date.parse(new Date()) / 100
    if (pending.indexOf(flagUrl) !== -1) {
        if (f) {
            f() // 执行取消操作
        } else {
            pending.splice(pending.indexOf(flagUrl), 1)// 把这条记录从数组中移除
        }
    } else {
        if (f) {
            pending.push(flagUrl)
        }
    }
}
axios.defaults.baseURL = configParams.apiPath;
//设置请求超时时间
axios.defaults.timeout = 5000

//开始请求设置，发起拦截处理 config代表发起请求的参数实体
axios.interceptors.request.use(config => {
    var index = '?'
    if (config.url.indexOf("?") > 0) {
        index = '&'
    }
    config.url += index + '&_t' + '=' + Date.parse(new Date()) / 100

    //请求拦截器
    config.cancelToken = new CancelToken((c) => {
        removePending(config, c)
    })
	config.headers["Content-Type"] ="application/json;charset=UTF-8";
    //免登录
    if (config.url.indexOf(configParams.jwtUrl) != -1) {
		config.headers[configParams.Authorization] = ykh.getVal("jwt");
		config.headers[configParams.HearderParamClubId] = ykh.getVal("clubId");
    }
    return config;  //添加这一行
}, error => {
    return error;
})

//请求到结果的拦截处理
var toLogin=0;
axios.interceptors.response.use(data => {
    // console.log(data) //统一打印返回值
    //返回请求的正确结果
    if(data.data && data.data.status=='9999'){
      localStorage.clear()
      // location.reload();
	  if(toLogin==0){
		  toLogin=1
	  	MessageBox('登录会话已过期,是否重新登录?', '提示', {
	  	  confirmButtonText: '确定',
	  	  cancelButtonText: '取消',
	  	  type: 'warning',
	  	  center: true
	  	}).then(() => {
	  	  y.go('/Login')
	  	  localStorage.clear()
		  return 
	  	}).catch(() => {
	  	});
	  }
    }else{
		toLogin=0
	}
    return data.data;
}, error => {
    //如果头部令牌已经无效
    if (error.response && error.response.data && error.response.data.error && error.response.data.error .indexOf("JWT expired")!=-1) {
		ykh.go('/')
	}

    if (error.response) {
        return error.response.data;
    }

})

ykh.confirm=function(title,content,onConfirm,conCancel){
	MessageBox(content, title, {
	  confirmButtonText: '确定',
	  cancelButtonText: '取消',
	  type: 'warning',
	  center: true
	}).then(() => {
		if(onConfirm){
			onConfirm()
		}
	}).catch(() => {
		if(conCancel){
			onCancel()
		}
	});
}

//将axios的get方法绑定到上面
ykh.get = function (url, params, callback) {
    return new Promise((resolve, reject) => {
        axios.get(url, { params: params })
            .then(res => {
                // resolve(res)
                callback(res)
            }).catch(err => {
                // callback(err)
                reject(err)
            })
    })
}

//将axios的post方法绑定到ykh上面
ykh.post = function (url, params, callback) {
    return new Promise((resolve, reject) => {
        axios.post(url, params)
            .then(res => {
                //resolve(res)
                callback(res)
            }).catch(err => {
                // callback(err)
                reject(err)
            })
    })
}


//将axios的patch方法绑定到ykh上面
ykh.patch = function (url, params, callback) {
    return new Promise((resolve, reject) => {
        axios.patch(url, params)
            .then(res => {
                // resolve(res)
                callback(res)
            }).catch(err => {
                // callback(err)
                reject(err)
            })
    })
}
//将axios的put方法绑定到ykh上面
ykh.put = function (url, params, callback) {
    return new Promise((resolve, reject) => {
        axios.put(url, params)
            .then(res => {
                // resolve(res)
                callback(res)
            }).catch(err => {
                // callback(err)
                reject(err)
            })
    })
}
//将axios的del方法绑定到ykh上面  删除的id 在url后 例如：/api/users/123  删除id为123的用户
ykh.del = function (url, callback) {
    return new Promise((resolve, reject) => {
        axios.delete(url, {})
            .then(res => {
                // resolve(res)
                callback(res)
            }).catch(err => {
                // callback(err)
                reject(err)
            })
    })
}
//将axios的delete方法绑定到S上面
ykh.delete = function (url, params, callback) {
    return new Promise((resolve, reject) => {
        axios.delete(url, params)
            .then(res => {
                // resolve(res)
                callback(res)
            }).catch(err => {
                // callback(err)
                reject(err)
            })
    })
}
/**********************************************************************vuex localStorage 组件间和缓存********************************************************************* */

export const hasChild = (item) => {
  return item.children && item.children.length !== 0
}

const showThisMenuEle = (item, access) => {
  if (item.meta && item.meta.access && item.meta.access.length) {
    if (hasOneOf(item.meta.access, access)) return true
    else return false
  } else return true
}
/**
 * @param {Array} target 目标数组
 * @param {Array} arr 需要查询的数组
 * @description 判断要查询的数组是否至少有一个元素包含在目标数组中
 */
export const hasOneOf = (targetarr, arr) => {
  return targetarr.some(_ => arr.indexOf(_) > -1)
}
//根据所有菜单遍历和用户的角色获取菜单
import routers from '../router/index.js';
export const forEach = (arr, fn) => {
  if (!arr.length || !fn) return
  let i = -1
  let len = arr.length
  while (++i < len) {
    let item = arr[i]
    fn(item, i, arr)
  }
}
ykh.getMenuList = function(routers,access){
	let res = []
	forEach(routers, item => {
	  if (!item.meta || (item.meta && !item.meta.hideInMenu)) {
	    let obj = {
	      icon: (item.meta && item.meta.icon) || '',
	      name: item.name,
	      meta: item.meta
	    }
	    if ((hasChild(item) || (item.meta && item.meta.showAlways)) && showThisMenuEle(item, access)) {
	      obj.children = ykh.getMenuList(item.children, access)
	    }
	    if (item.meta && item.meta.href) obj.href = item.meta.href
	    if (showThisMenuEle(item, access)) res.push(obj)
	  }
	})
	return res
}



/**
 * 取值
 * @param {} key
 */
ykh.getVal = function (key) {
    // return store.state[key];
    return localStorage.getItem(key);

}
/**
 *
 * @param {key} key
 * @param {值} value
 */
ykh.setVal = function (key, value) {
    localStorage.setItem(key, value);
}
/******************************************************************************** i18n 国际化 ********************************************************************************/

/******************************************************************路由跳转************************************************************************* */
//路由
import router from '../router'

/**
 * 原生路由跳转
 */
ykh.go = function(params){
    router.push(params)
}
/**从新窗口打开 */
ykh.gonew = function(url){
    // 新窗口打开
    var goRouter = router.resolve({
        name: url,
    });
    window.open(goRouter.location.name,'_blank');
}
/**
 * 跳转传递参数 例如：/users?id=1   s.goQ("/users",{id:1})
 */
ykh.goQ = function(url,params){
    router.push({path:url,query:params})
}
//Vue.use(router)
ykh.router = router;



/********************************************************************list转换 树形结构****************************************************************************/


/**
* json格式转标准树状结构
* @param {json} json数据 $.parseJSON()  list数据 [{},{}]
* @param {String} id的字符串 属性名称  根据这个找到主键  一般是 id
* @param {String} 父id的字符串 属性名称  根据这个找到叶子节点   一般是 parentId
* @param {String} children的字符串 属性名称  例如：children
* @param {open} 是否展开  1展开
* @return {Array} 树形数组
*/
ykh.parseTree = function(json, idStr, pidStr, childrenStr,open,point,name){
    /****默认值：结合后端树形表结构 id,parentId,parentIds 同时结合前端iview children   组装为树形结构****/
    if(!idStr){
        idStr = "id"
    }
    if(!pidStr){
        pidStr = "parentId"
    }
    if(!childrenStr){
        childrenStr = "children"
    }
    if(open&&open=='1'){
        open = false
    }else if(open&&open=='0'){
        open = true
    }
    if(!point){
    	point=""
    }

    var r = [], hash = {}, id = idStr, pid = pidStr, children = childrenStr, i = 0, j = 0, len = json.length;
    for(; i < len; i++){
    	json[i][children] = []
        hash[json[i][id]] = json[i];
    }

    for(; j < len; j++){
        var aVal = json[j], hashVP = hash[aVal[pid]];

        if(!name){
        	aVal['title'] = aVal['name'] + point;//name 放入title  iviewTree使用
        }else{

        	aVal['title'] = aVal['name'] + point + aVal[name];//name 放入title  iviewTree使用
    		if(aVal['isLeaf'] && aVal['isLeaf'] == '1'){//最后一级
    			var allTitle ='' //所有称
    			 for(var l=0; l < len; l++){
    				 var aleafVal = json[l]
    				 if(aVal['parentIds'].indexOf(aleafVal['id']) != -1){//包含
    					 allTitle  = allTitle + aleafVal['name'] + point
    				 }
    			 }
    			aVal['allTitle']  = allTitle

    		}
        }
        if(open){
        	 aVal['expand'] = open;//默认全部展开
        }
        if(hashVP){
            !hashVP[children] && (hashVP[children] = []);
            hashVP[children].push(aVal);
        }else{
            r.push(aVal);
        }
    }
    return r;
}

/*********************************************************************list转换 树形结构****************************************************************************/

/**
 * MD5
 */


/**
 * 返回uuid
 */
ykh.uuid = function () {
    function S4() {
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}



/*****************************************************************相对时间显示************************************************************************** */
ykh.date = function(time){
    let oldDate = new Date(time)
    let newDate = new Date()
    var dayNum = "";
    var getTime = (newDate.getTime() - oldDate.getTime())/1000;
     if(getTime < 60){
        dayNum = "现在"; // just now
    }else if(getTime >= 60*1 && getTime < 60*60){
        dayNum = parseInt(getTime / 60) + s.t('base.date.minutesAgo'); //minutes ago
    }else if(getTime >= 3600 && getTime < 3600*24){
        dayNum = parseInt(getTime / 3600) + s.t('base.date.hoursAgo'); // hours ago
    }else if(getTime >= 3600 * 24 && getTime < 3600 * 24 * 30){
        dayNum = parseInt(getTime / 3600 / 24 ) + s.t('base.date.dayAgo'); // day ago
    }else if(getTime >= 3600 * 24 * 30 && getTime < 3600 * 24 * 30 * 12){
        dayNum = parseInt(getTime / 3600 / 24 / 30 ) + s.t('base.date.monthAgo');  // month ago
    }else if(time >= 3600 * 24 * 30 * 12){
        dayNum = parseInt(getTime / 3600 / 24 / 30 / 12 ) + s.t('base.date.yearsAgo');  //years ago
    }
    return dayNum;
}
/***********************************************传入时间戳，转换为yyyy-mm-dd**********************************/
ykh.fmtDate = function(obj){
    var date =  new Date(obj);
    var y = 1900+date.getYear();
    var m = "0"+(date.getMonth()+1);
    var d = "0"+date.getDate();
    return y+"-"+m.substring(m.length-2,m.length)+"-"+d.substring(d.length-2,d.length);
}

ykh.getNowDate = function getNowDate() {
	 var date = new Date();
	 var sign1 = "-";
	 var sign2 = ":";
	 var year = date.getFullYear() // 年
	 var month = date.getMonth() + 1; // 月
	 var day  = date.getDate(); // 日
	 var hour = date.getHours(); // 时
	 var minutes = date.getMinutes(); // 分
	 var seconds = date.getSeconds() //秒
//	 var weekArr = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期天'];
//	 var week = weekArr[date.getDay()];
	 // 给一位数数据前面加 “0”
	 if (month >= 1 && month <= 9) {
	  month = "0" + month;
	 }
	 if (day >= 0 && day <= 9) {
	  day = "0" + day;
	 }
	 if (hour >= 0 && hour <= 9) {
	  hour = "0" + hour;
	 }
	 if (minutes >= 0 && minutes <= 9) {
	  minutes = "0" + minutes;
	 }
	 if (seconds >= 0 && seconds <= 9) {
	  seconds = "0" + seconds;
	 }
	 var currentdate = year + sign1 + month + sign1 + day + " " + hour + sign2 + minutes + sign2 + seconds ;
	 return currentdate;
}
/***************************************************************** jssdk ************************************************************************* */




/**
 * 定义一个map
 * 如：var map=new s.HashMap();
 * */
ykh.HashMap=function(){
	//定义长度
	var length = 0;
	//创建一个对象
	var obj = new Object();

	/**
	* 判断Map是否为空
	*/
	this.isEmpty = function(){
		return length == 0;
	};

	/**
	* 判断对象中是否包含给定Key
	*/
	this.containsKey=function(key){
		return (key in obj);
	};

	/**
	* 判断对象中是否包含给定的Value
	*/
	this.containsValue=function(value){
		for(var key in obj){
			if(obj[key] == value){
				return true;
			}
		}
		return false;
	};

	/**
	*向map中添加数据
	*/
	this.put=function(key,value){
		if(!this.containsKey(key)){
			length++;
		}
		obj[key] = value;
	};

	/**
	*向map原数据 添加数据
	*/
	this.putAll=function(key,value){
		if(!this.containsKey(key)){
			length++;
		}
		if(this.get(key)){
			obj[key] = this.get(key)+','+value;
		}else{
			obj[key] = value;
		}
	};

	/**
	* 根据给定的Key获得Value
	*/
	this.get=function(key){
		return this.containsKey(key)?obj[key]:null;
	};

	/**
	* 根据给定的Key删除一个值
	*/
	this.remove=function(key){
		if(this.containsKey(key)&&(delete obj[key])){
			length--;
		}
	};

	/**
	* 获得Map中的所有Value
	*/
	this.values=function(){
		var _values= new Array();
		for(var key in obj){
			_values.push(obj[key]);
		}
		return _values;
	};

	/**
	* 获得Map中的所有Key
	*/
	this.keySet=function(){
		var _keys = new Array();
		for(var key in obj){
			_keys.push(key);
		}
		return _keys;
	};

	/**
	* 获得Map的长度
	*/
	this.size = function(){
		return length;
	};

	/**
	* 清空Map
	*/
	this.clear = function(){
		length = 0;
		obj = new Object();
	};
};



export default ykh
