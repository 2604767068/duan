<template>
	<d2-container>
      <el-card>
		  <el-card class="box-card" >
		  	<h2 style="float:left" type="primary">社团留言</h2>
		  	<el-button style="float:right;margin-left:3px" type="primary" @click="dialogFormVisible=true">发布社团留言</el-button>
			<el-button style="float:right;margin-left:3px" type="primary" @click="dialogFormVisible1=true">发布网站留言</el-button>
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
	  
	  <el-dialog title="发布社团留言" :visible.sync="dialogFormVisible">
	    <el-form  label-width="100px">
	  		<el-form-item label="留言内容" >
	  			<el-input v-model="message.message" autocomplete="off"></el-input>
	  		</el-form-item>
	    </el-form>
	    <div slot="footer" class="dialog-footer">
	      <el-button @click="dialogFormVisible = false">取 消</el-button>
	      <el-button type="primary" @click="createActivity">确 定</el-button>
	    </div>
	  </el-dialog>
	  
	  <el-dialog title="发布网站留言" :visible.sync="dialogFormVisible1">
	    <el-form  label-width="100px">
	  		<el-form-item label="留言内容" >
	  			<el-input v-model="message.message" autocomplete="off"></el-input>
	  		</el-form-item>
	    </el-form>
	    <div slot="footer" class="dialog-footer">
	      <el-button @click="dialogFormVisible = false">取 消</el-button>
	      <el-button type="primary" @click="createMessage">确 定</el-button>
	    </div>
	  </el-dialog>
	  
	</d2-container>
</template>

<script>
	export default{
		data(){
			return{
				dialogFormVisible1:false,
				dialogFormVisible:false,
				tableData:[],
				page:{
				  size:10,
				  current:1,
				  total:0
				},
				message:{}
			}
		},
		methods:{
			createMessage(){
				if(!this.message.message){
					this.$message.error("请输入留言内容")
					return false
				}
				var that=this
				y.post('/api/jwt/clubMessage/message',this.message,function(data){
					if(data && data.success){
						that.$message.success("操作成功")
						that.dialogFormVisible1=false
						that.getTableData()
					}else{
						that.$message.error(data.message)
					}
				})
			},
			createActivity(){
				if(!this.message.message){
					this.$message.error("请输入留言内容")
					return false
				}
				var that=this
				y.post('/api/jwt/clubMessage/clubMessage',this.message,function(data){
					if(data && data.success){
						that.$message.success("操作成功")
						that.dialogFormVisible=false
						that.getTableData()
					}else{
						that.$message.error(data.message)
					}
				})
			},
			addData(){
				this.page.size+=10
				this.getTableData()
			},
			getTableData(){
				var that=this
				y.get('/api/jwt/clubMessage/clubMessage',this.page,function(data){
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
