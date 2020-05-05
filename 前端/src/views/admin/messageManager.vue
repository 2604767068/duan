<template>
	<d2-container>
     <el-card>
     		  <el-card class="box-card" >
     		  	<h2 style="float:left" type="primary">网站留言</h2>
     		  </el-card>
     		<el-alert
     		v-for="(item,index) in tableData"
     		    :title="item.createName+'  '+item.createDate"
     		    type="success"
     			:closable="false"
     		    :description="item.message">
     		  </el-alert>
     		  <el-row v-if="tableData.length<page.total">
     		  	<el-divider @click="addData">加载更多</el-divider>
     		  </el-row>
     		  <el-row v-else>
     		  	<el-divider>暂无更多数据</el-divider>
     		  </el-row>
     </el-card>
	</d2-container>
</template>

<script>
	export default{
		data(){
			return{
				page:{
					size:10,
					current:1,
					total:0,
				},
				tableData:[]
			}
		},
	methods:{
		addData(){
				this.page.size+=10
				this.getTableData()
			},
			getTableData(){
				var that=this
				y.get('/api/jwt/clubManager/message',this.page,function(data){
					if(data && data.success){
						that.tableData=data.data.records
						that.page.total=data.data.total
					}else{
						that.tableData=[]
					}
				})
			},
		},
		created(){
			this.getTableData()
		}
	}
</script>

<style>
</style>
