<template>
  <d2-container class="page">
		<el-row>
		  <el-col :span="8">
			  <el-card>
				  <d2-icon name="user-circle" style="font-size: 50px;color: greenyellow;"></d2-icon>
				  <h3>总会员：{{report.userNum}}</h3>
			  </el-card>
		  </el-col>
		  <el-col :span="8">
			  <el-card class="box-card">
				  <d2-icon name="cc-diners-club " style="font-size: 50px;color: firebrick;"></d2-icon>
				  <h3>总社团：{{report.clubNum}}</h3>
			  </el-card>
		  </el-col>
		  <el-col :span="8">
			  <el-card class="box-card">
				  <d2-icon name="calculator" style="font-size: 50px;color: skyblue;"></d2-icon>
				  <h3>策划活动：{{report.activityNum}}场</h3>
			  </el-card>
		  </el-col>
		</el-row>
		<el-row style="top:100px;">
		  <el-col :span="8">
			  <el-card>
				  <d2-icon name="user-circle" style="font-size: 50px;color: lightgreen;"></d2-icon>
				  <h3>今日新增会员：{{report.todayUserNum}}</h3>
			  </el-card>
		  </el-col>
		  <el-col :span="8">
			  <el-card class="box-card">
				  <d2-icon name="cc-diners-club " style="font-size: 50px;color: palevioletred;"></d2-icon>
				  <h3>今日新增社团：{{report.todayClubNum}}</h3>
			  </el-card>
		  </el-col>
		  <el-col :span="8">
			  <el-card class="box-card">
				  <d2-icon name="calculator" style="font-size: 50px;color: royalblue;"></d2-icon>
				  <h3>今日新增活动：{{report.todayActivityNum}}场</h3>
			  </el-card>
		  </el-col>
		</el-row>
      <!-- <d2-icon-svg class="logo" name="d2-admin"/> -->
<!--      <template slot="footer">
        <div class="btn-group">
          <span class="btn-group__btn" @click="$open('https://github.com/d2-projects')">开源组织</span> |
          <span class="btn-group__btn" @click="$open('https://d2.pub/zh/doc/d2-admin')">文档</span> |
          <span class="btn-group__btn" @click="$open('https://github.com/d2-projects/d2-admin-start-kit')">简化版</span> |
          <span class="btn-group__btn" @click="$open('https://juejin.im/user/57a48b632e958a006691b946/posts')">掘金</span> |
          <el-popover :width="172" trigger="hover">
            <p class="d2-mt-0 d2-mb-10">今日前端</p>
            <img src="./image/qr@2x.png" style="width: 172px;">
            <span slot="reference" class="btn-group__btn btn-group__btn--link">
              <d2-icon name="weixin"/>
              微信公众号
            </span>
            <p style="font-size: 12px; margin-top: 0px; margin-bottom: 0px;">
              官方公众号，主要推送前端技术类文章、框架资源、学习教程，以及 D2 系列项目更新信息
            </p>
          </el-popover>
        </div>
        <d2-badge/>
        <d2-help/>
      </template> -->
  </d2-container>
</template>

<script>
import D2Badge from './components/d2-badge'
import D2Help from './components/d2-help'
import D2PageCover from './components/d2-page-cover'
export default {
  components: {
    D2Badge,
    D2Help,
    D2PageCover
  },
  data () {
    return {
      filename: __filename,
	  report:{}
    }
  },
	methods:{
		getIndexReport(){
			var that=this
			y.get('/api/jwt/indexNews/indexReport',{},function(data){
				if(data && data.success){
					that.report=data.data
				}else{
					that.report={}
				}
			})
		}
	},
	created(){
		this.getIndexReport()
	}
}
</script>

<style lang="scss" scoped>
.page {
  .logo {
    width: 120px;
  }
  .btn-group {
    color: $color-text-placehoder;
    font-size: 12px;
    line-height: 12px;
    margin-top: 0px;
    margin-bottom: 20px;
    .btn-group__btn {
      color: $color-text-sub;
      &:hover {
        color: $color-text-main;
      }
      &.btn-group__btn--link {
        color: $color-primary;
      }
    }
  }
}
</style>
