<template>
  <d2-container class="page">
			<el-row>
			  <el-col :span="12">
				<el-card>
					<h3>首页新闻</h3>
				</el-card>
				 <el-collapse v-model="indexActiveNames" @change="handleChange">
				   <el-collapse-item v-for="item in indexNews" :key="item.id" :title="item.title" :name="item.id">
				      <el-image :src="item.images"></el-image>
						<div>{{item.content}}</div>
				   </el-collapse-item>
				 </el-collapse>
			  </el-col>
			  <el-col :span="12">
				  <el-card>
					  <h3>社团新闻</h3>
				  </el-card>
				   <el-collapse v-model="clubActiveNames" @change="handleChange">
				     <el-collapse-item v-for="item in clubNews" :key="item.id" :title="item.title" :name="item.id">
				        <el-image :src="item.images"></el-image>
					   <div>{{item.content}}</div>
				     </el-collapse-item>
				   </el-collapse>
			  </el-col>
			</el-row>
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
	  clubNews:[],
	  indexNews:[],
	  clubActiveNames:[],
	  indexActiveNames:[]
    }
  },
	methods:{
		handleChange(){
			
		},
		getClubNews(){
			var that=this
			var page={
				current:1,
				size:100
			}
			y.get('/api/jwt/clubNews/clubNews?status=0',page,function(data){
				if(data && data.success){
					that.clubNews=data.data.records
					var len=data.data.records.length
					for(var i=0;i<len;i++){
						if(that.clubNews[i].images){
							that.clubNews[i].images=require("../../../../static/img/upload/"+that.clubNews[i].images)
						}
					}
					that.clubActiveNames.push(that.clubNews[0].id)
				}else{
					that.clubNews=[]
				}
			})
		},
		getIndexNews(){
			var that=this
			var page={
				current:1,
				size:100
			}
			y.get('/api/jwt/indexNews/indexNews?status=0',page,function(data){
				if(data && data.success){
					that.indexNews=data.data.records
					that.indexActiveNames.push(that.indexNews[0].id)
					var len=data.data.records.length
					for(var i=0;i<len;i++){
						if(that.indexNews[i].images){
							that.indexNews[i].images=require("../../../../static/img/upload/"+that.indexNews[i].images)
						}
					}
				}else{
					that.indexNews=[]
				}
			})
		}
	},
	created(){
		this.getClubNews()
		this.getIndexNews()
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
