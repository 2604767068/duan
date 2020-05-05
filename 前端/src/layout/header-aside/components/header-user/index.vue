<template>
  <el-dropdown size="small" class="d2-mr">
    <span class="btn-text">{{userInfo.name ? `你好 ${userInfo.name}` : '未登录'}}</span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item @click.native="logOff">
        <d2-icon name="power-off" class="d2-mr-5"/>
        注销
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { mapState, mapActions } from 'vuex'
export default {
  data() {
	  return{
		  userInfo:{}
	  }
  },
  methods: {
    ...mapActions('d2admin/account', [
      'logout'
    ]),
    /**
     * @description 登出
     */
    logOff () {
		localStorage.clear()
		y.go("/login")
    }
  },
  created(){
	  var role=y.getVal("role")
	  var that=this
	  if(role=='admin'){
	  	y.get('/api/jwt/user/adminInfo',{},function(data){
	  		if(data && data.success){
	  			y.setVal("userInfo",JSON.stringify(data.data))
				that.userInfo=data.data
	  		}else{
	  			
	  		}
	  	})
	  }else{
	  	 y.get('/api/jwt/user/userInfo',{},function(data){
	  	 	if(data && data.success){
	  	 		y.setVal("userInfo",JSON.stringify(data.data))
				that.userInfo=data.data
	  	 	}else{
	  	 		
	  	 	}
	  	 })
	  }
  }
}
</script>
