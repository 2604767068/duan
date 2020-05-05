<template>
  <div class="page-login">
    <div class="page-login--layer page-login--layer-area">
      <ul class="circles">
        <li v-for="n in 10" :key="n"></li>
      </ul>
    </div>
    <div
      class="page-login--layer page-login--layer-time"
      flex="main:center cross:center">
      {{time}}
    </div>
    <div class="page-login--layer">
      <div
        class="page-login--content"
        flex="dir:top main:justify cross:stretch box:justify">
        <div class="page-login--content-header">
          <p class="page-login--content-header-motto">
            学生社团管理系统
          </p>
        </div>
        <div
          class="page-login--content-main"
          flex="dir:top main:center cross:center">
          <!-- logo -->
          <!-- <img class="page-login--logo" src="./image/logo@2x.png"> -->
          <!-- form -->
		   <div class="page-login--form">
		   <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
		      <el-tab-pane label="用户登录" name="userLogin">
				    <el-card shadow="never" v-if="formType=='login'">
				      <el-form
				        ref="loginForm"
				        label-position="top"
				        :rules="rules"
				        :model="formLogin"
				        size="default">
				        <el-form-item prop="username">
				          <el-input
				            type="text"
				            v-model="user.email"
				            placeholder="用户名">
				            <i slot="prepend" class="fa fa-user-circle-o"></i>
				          </el-input>
				        </el-form-item>
				        <el-form-item prop="password">
				          <el-input
				            type="password"
				            v-model="user.password"
				            placeholder="密码" @keyup.enter="submit">
				            <i slot="prepend" class="fa fa-keyboard-o"></i>
				          </el-input>
				        </el-form-item>
				        <el-button
				          size="default"
				          @click="submit"
				          type="primary"
				          class="button-login">
				          登录
				        </el-button>
				      </el-form>
				    </el-card>
					<el-card shadow="never" v-if="formType=='regist'">
					  <el-form
					    ref="loginForm"
					    label-position="top"
					    :rules="rules"
					    :model="formLogin"
					    size="default">
					    <el-form-item>
					      <el-input
					        type="text"
					        v-model="user.name"
					        placeholder="用户名">
					        <i slot="prepend" class="fa fa-user-circle-o"></i>
					      </el-input>
					    </el-form-item>
					    <el-form-item>
					      <el-input
					        type="password"
					        v-model="user.password"
					        placeholder="密码">
					        <i slot="prepend" class="fa fa-keyboard-o"></i>
					      </el-input>
					    </el-form-item>
						<el-form-item>
						  <el-input
						    type="text"
						    v-model="user.email"
						    placeholder="邮箱">
						    <i slot="prepend" class="fa fa-telegram"></i>
						  </el-input>
						</el-form-item>
						<el-form-item>
						  <el-input
						    type="text"
						    v-model="user.code"
						    placeholder="验证码">
							<el-button slot="append"  :disabled="disable" @click="getCode()">获取验证码{{codeTime}}</el-button>
						  </el-input>
						</el-form-item>
					    <el-button
					      size="default"
					      @click="registUser"
					      type="primary"
					      class="button-login">
					      注册
					    </el-button>
					  </el-form>
					</el-card>
					<el-card shadow="never" v-if="formType=='codeLogin'">
					  <el-form
					    ref="loginForm"
					    label-position="top"
					    :rules="rules"
					    :model="formLogin"
					    size="default">
					    <el-form-item>
					      <el-input
					        type="text"
					        v-model="user.email"
					        placeholder="邮箱">
					        <i slot="prepend" class="fa fa-user-circle-o"></i>
					      </el-input>
					    </el-form-item>
						<el-form-item>
						  <el-input
						    type="text"
						    v-model="user.code"
						    placeholder="验证码">
							<el-button slot="append"  :disabled="disable" @click="getCodeConfirm()">获取验证码{{codeTime}}</el-button>
						  </el-input>
						</el-form-item>
					    <el-button
					      size="default"
					      @click="codeLogin()"
					      type="primary"
					      class="button-login">
					      登录
					    </el-button>
					  </el-form>
					</el-card>
				    <p
				      class="page-login--options"
				      flex="main:justify cross:center">
				      <span v-if="formType=='login'" @click="formType='codeLogin'"> 验证码登录</span>
					   <span  v-if="formType=='codeLogin'" @click="formType='login'">密码登录</span>
				      <span v-if="formType=='login'" @click="formType='regist'">注册用户</span>
					  <span v-if="formType=='regist'" @click="formType='login'">返回登录</span>
				    </p>
			  </el-tab-pane>
		      <el-tab-pane label="管理员登录" name="adminLogin">
				    <el-card shadow="never">
				      <el-form
				        ref="loginForm"
				        label-position="top"
				        :rules="rules"
				        :model="formLogin"
				        size="default">
				        <el-form-item prop="username">
				          <el-input
				            type="text"
				            v-model="admin.username"
				            placeholder="用户名">
				            <i slot="prepend" class="fa fa-user-circle-o"></i>
				          </el-input>
				        </el-form-item>
				        <el-form-item prop="password">
				          <el-input
				            type="password"
				            v-model="admin.password"
				            placeholder="密码" @keyup.enter="adminLogin">
				            <i slot="prepend" class="fa fa-keyboard-o"></i>
				          </el-input>
				        </el-form-item>
				        <el-button
				          size="default"
				          @click="adminLogin"
				          type="primary"
				          class="button-login">
				          登录
				        </el-button>
				      </el-form>
				    </el-card>
				 
			  </el-tab-pane>
		    </el-tabs>
         </div>
        </div>
        <div class="page-login--content-footer">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import dayjs from 'dayjs'
import { mapActions } from 'vuex'
import localeMixin from '@/locales/mixin.js'
export default {
  mixins: [
    localeMixin
  ],
  data () {
    return {
		disable:false,
		codeTime:'',
		user:{},
		formType:'login',
		activeName:'userLogin',
      timeInterval: null,
      time: dayjs().format('HH:mm:ss'),
      // 快速选择用户
      // 表单
      formLogin: {
        username: 'admin',
        password: 'admin'
      },
	  admin:{},
      // 表单校验
      rules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  mounted () {
    this.timeInterval = setInterval(() => {
      this.refreshTime()
    }, 1000)
  },
  beforeDestroy () {
    clearInterval(this.timeInterval)
  },
  methods: {
	  getCodeConfirm(){
		  var that=this
		  if(!this.user.email){
		  	 this.$message.error("请输入邮箱")
		  	 return false
		  }
		  y.post('/api/open/user/isRegist?email='+this.user.email,{},function(data){
			  if(data && data.success){
				  that.getCode()
			  }else{
				  that.$message.error("该邮箱未注册，请先注册")
				  that.formType='regist'
			  }
		  })
	  },
	  codeLogin(){
		  if(!this.user.email){
		  	 this.$message.error("请输入邮箱")
		  	 return false
		  }
		  if(!this.user.code){
		  	 this.$message.error("请输入验证码")
		  	 return false
		  }
		  var that=this
		  y.post('/api/open/user/codeLogin',this.user,function(data){
			  if(data && data.success){
				   y.setVal("jwt",data.attributes.token)
				   that.$message.success("登录成功")
					  y.setVal("role","user")
				   // 重定向对象不存在则返回顶层路径
				   that.$router.push("/")
				}else{
				   that.$message.error("账号或密码错误")
			  }
		  })
	  },
	  registUser(){
		  if(!this.user.name){
		  	 this.$message.error("请输入用户名")
		  	 return false
		  }
		  if(!this.user.password){
		  	 this.$message.error("请输入密码")
		  	 return false
		  }
		  if(!this.user.email){
		  	 this.$message.error("请输入邮箱")
		  	 return false
		  }
		  if(!this.user.code){
		  	 this.$message.error("请输入验证码")
		  	 return false
		  }
		  var that=this;
		  y.post('/api/open/user/regist',this.user,function(data){
			  if(data && data.success){
				  that.$message.success("注册成功,马上自动登录")
				  setTimeout(function(){
					y.post('/api/open/user/login',that.user,function(data){
						   if(data && data.success){
							   y.setVal("jwt",data.attributes.token)
							   that.$message.success("登录成功")
								  y.setVal("role","user")
							   // 重定向对象不存在则返回顶层路径
							   that.$router.push("/")
						   }else{
							   that.$message.error("账号或密码错误")
						   }
					})
				  },1000)
			  }else if(data.status='10202'){
				  that.$message.error("该邮箱已注册")
			  }else{
				  that.$message.error(data.message)
			  }
		  })
	  },
	  handleClick(tab, event){
		  this.activeName=tab.name
	  },
    ...mapActions('d2admin/account', [
      'login'
    ]),
    refreshTime () {
      this.time = dayjs().format('HH:mm:ss')
    },
	getCode(){
		if(!this.user.email){
			 this.$message.error("请输入邮箱")
			 return false
		}
		if(this.user.email.indexOf("@")==-1){
			this.$message.error("请输入正确的邮箱")
			return false
		}
		var that=this;
		y.post('/api/open/user/sendEmail?email='+this.user.email,{},function(data){
			if(data && data.success){
				that.$message.success("发送成功");
				that.disable=true
				that.codeTime=10
				var timeInterval = setInterval(() => {
					that.codeTime--;
					if(that.codeTime==0){
						that.codeTime=''
						that.disable=false
						clearInterval(timeInterval)
					}
				}, 1000)
			}else{
				that.$message.error(data.message);
			}
		})
		
	 },
	 adminLogin(){
		 var that=this;
		 if(!this.admin.username){
			 this.$message.error("请输入账号")
			 return false
		 }
		 if(!this.admin.password){
			 this.$message.error("请输入密码")
			 return false
		 }
		 y.post('/api/open/user/adminLogin?username='+that.admin.username+"&password="+that.admin.password,{},function(data){
		 	   if(data && data.success){
		 		   y.setVal("jwt",data.attributes.token)
		 		   y.setVal("role","admin")
		 		   that.$message.success("登录成功")
		 		   // 重定向对象不存在则返回顶层路径
		 		   that.$router.push("/adminIndex")
		 	   }else{
		 		   that.$message.error("账号或密码错误")
		 	   }
		 })
	 },
    /**
     * @description 提交表单
     */
    // 提交登录信息
    submit () {
		var that=this;
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          // 登录
          // 注意 这里的演示没有传验证码
          // 具体需要传递的数据请自行修改代码
           y.post('/api/open/user/login',that.user,function(data){
			   if(data && data.success){
				   y.setVal("jwt",data.attributes.token)
				   y.setVal("role","user")
				   that.$message.success("登录成功")
				   // 重定向对象不存在则返回顶层路径
				   that.$router.push("/")
			   }else{
				   that.$message.error("账号或密码错误")
			   }
		})
        } else {
          // 登录表单校验失败
          this.$message.error('表单校验失败，请检查')
        }
      })
    }
  }
}
</script>

<style lang="scss">
.page-login {
  @extend %unable-select;
  $backgroundColor: #F0F2F5;
  // ---
  background-color: $backgroundColor;
  height: 100%;
  position: relative;
  // 层
  .page-login--layer {
    @extend %full;
    overflow: auto;
  }
  .page-login--layer-area {
    overflow: hidden;
  }
  // 时间
  .page-login--layer-time {
    font-size: 24em;
    font-weight: bold;
    color: rgba(0, 0, 0, 0.03);
    overflow: hidden;
  }
  // 登陆页面控件的容器
  .page-login--content {
    height: 100%;
    min-height: 500px;
  }
  // header
  .page-login--content-header {
    padding: 1em 0;
    .page-login--content-header-motto {
      margin: 0px;
      padding: 0px;
      color: $color-text-normal;
      text-align: center;
      font-size: 12px;
    }
  }
  // main
  .page-login--logo {
    width: 240px;
    margin-bottom: 2em;
    margin-top: -2em;
  }
  // 登录表单
  .page-login--form {
    width: 280px;
    // 卡片
    .el-card {
      margin-bottom: 15px;
    }
    // 登录按钮
    .button-login {
      width: 100%;
    }
    // 输入框左边的图表区域缩窄
    .el-input-group__prepend {
      padding: 0px 14px;
    }
    .login-code {
      height: 40px - 2px;
      display: block;
      margin: 0px -20px;
      border-top-right-radius: 2px;
      border-bottom-right-radius: 2px;
    }
    // 登陆选项
    .page-login--options {
      margin: 0px;
      padding: 0px;
      font-size: 14px;
      color: $color-primary;
      margin-bottom: 15px;
      font-weight: bold;
    }
    .page-login--quick {
      width: 100%;
    }
  }
  // 快速选择用户面板
  .page-login--quick-user {
    @extend %flex-center-col;
    padding: 10px 0px;
    border-radius: 4px;
    &:hover {
      background-color: $color-bg;
      i {
        color: $color-text-normal;
      }
      span {
        color: $color-text-normal;
      }
    }
    i {
      font-size: 36px;
      color: $color-text-sub;
    }
    span {
      font-size: 12px;
      margin-top: 10px;
      color: $color-text-sub;
    }
  }
  // footer
  .page-login--content-footer {
    padding: 1em 0;
    .page-login--content-footer-locales {
      padding: 0px;
      margin: 0px;
      margin-bottom: 15px;
      font-size: 12px;
      line-height: 12px;
      text-align: center;
      color: $color-text-normal;
      a {
        color: $color-text-normal;
        margin: 0 .5em;
        &:hover {
          color: $color-text-main;
        }
      }
    }
    .page-login--content-footer-copyright {
      padding: 0px;
      margin: 0px;
      margin-bottom: 10px;
      font-size: 12px;
      line-height: 12px;
      text-align: center;
      color: $color-text-normal;
      a {
        color: $color-text-normal;
      }
    }
    .page-login--content-footer-options {
      padding: 0px;
      margin: 0px;
      font-size: 12px;
      line-height: 12px;
      text-align: center;
      a {
        color: $color-text-normal;
        margin: 0 1em;
      }
    }
  }
  // 背景
  .circles {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
    margin: 0px;
    padding: 0px;
    li {
      position: absolute;
      display: block;
      list-style: none;
      width: 20px;
      height: 20px;
      background: #FFF;
      animation: animate 25s linear infinite;
      bottom: -200px;
      @keyframes animate {
        0%{
          transform: translateY(0) rotate(0deg);
          opacity: 1;
          border-radius: 0;
        }
        100%{
          transform: translateY(-1000px) rotate(720deg);
          opacity: 0;
          border-radius: 50%;
        }
      }
      &:nth-child(1) {
        left: 15%;
        width: 80px;
        height: 80px;
        animation-delay: 0s;
      }
      &:nth-child(2) {
        left: 5%;
        width: 20px;
        height: 20px;
        animation-delay: 2s;
        animation-duration: 12s;
      }
      &:nth-child(3) {
        left: 70%;
        width: 20px;
        height: 20px;
        animation-delay: 4s;
      }
      &:nth-child(4) {
        left: 40%;
        width: 60px;
        height: 60px;
        animation-delay: 0s;
        animation-duration: 18s;
      }
      &:nth-child(5) {
        left: 65%;
        width: 20px;
        height: 20px;
        animation-delay: 0s;
      }
      &:nth-child(6) {
        left: 75%;
        width: 150px;
        height: 150px;
        animation-delay: 3s;
      }
      &:nth-child(7) {
        left: 35%;
        width: 200px;
        height: 200px;
        animation-delay: 7s;
      }
      &:nth-child(8) {
        left: 50%;
        width: 25px;
        height: 25px;
        animation-delay: 15s;
        animation-duration: 45s;
      }
      &:nth-child(9) {
        left: 20%;
        width: 15px;
        height: 15px;
        animation-delay: 2s;
        animation-duration: 35s;
      }
      &:nth-child(10) {
        left: 85%;
        width: 150px;
        height: 150px;
        animation-delay: 0s;
        animation-duration: 11s;
      }
    }
  }
}
</style>
