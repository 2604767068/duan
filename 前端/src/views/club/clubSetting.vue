<template>
	<d2-container>
		<el-card class="box-card" >
			<h2 style="float:left" type="primary">社团设置</h2>
			<el-button style="float:right;margin-left:3px" v-if="clubAdmin==1" type="primary" @click="dialogFormVisible=true">新建新闻</el-button>
		</el-card>
		<el-table
		  :data="tableData"
		  style="width: 100%">
		  <el-table-column
		    type="index"
		    :index="indexMethod">
		  </el-table-column>
		  <el-table-column
			prop="title"
		    label="标题"
		    >
		  </el-table-column>
			<el-table-column
			  label="状态"
			  >
			  <template slot-scope="scope">
				  <el-tag v-if="scope.row.status==0" size="medium" type="success">正常</el-tag>
				   <el-tag v-if="scope.row.status==1" size="medium" type="info">停用</el-tag>
			  </template>
			</el-table-column>
		  <el-table-column
		    label="图片"
		    width="180">
			  <template slot-scope="scope">
				  <el-avatar size="large" :src="scope.row.images"></el-avatar>
			  		  <!-- <img :src="scope.row.avatar"> -->
			  </template>
		  </el-table-column>
		  <el-table-column
		    label="内容"
			  width="180">
			     <template slot-scope="scope">
			          <el-popover trigger="hover" placement="top">
			            <p>{{ scope.row.content }}</p>
			            <div slot="reference" class="name-wrapper">
			              <el-tag size="medium">{{ scope.row.content }}</el-tag>
			            </div>
			          </el-popover>
			        </template>
		  </el-table-column>
			<el-table-column
			  prop="createDate"
			  label="创建时间"
			  >
			</el-table-column>
			   <el-table-column
			      fixed="right"
			      label="操作"
			      width="100">
			      <template slot-scope="scope">
			        <el-button v-if="clubAdmin=='1'" @click="deleteNews(scope.row.id)" type="text" size="small">删除</el-button>
					 <el-button v-if="scope.row.status==1 && clubAdmin=='1'" @click="editNews(scope.row.id,0)" type="text" size="small">启用</el-button>
					<el-button v-if="scope.row.status==0  && clubAdmin=='1'" @click="editNews(scope.row.id,1)" type="text" size="small">停用</el-button>
			      </template>
			    </el-table-column>
		</el-table>
		<el-pagination
		  style="float: right;"
		  :current-page="page.current"
		  :page-size="page.size"
		  :page-sizes="[10, 20, 50, 100]"
		   @size-change="handleSizeChange"
		   layout="sizes,prev,pager,next"
		  @current-change="changePage"
		  :total="page.total">
		</el-pagination>
  
  <el-dialog title="创建新闻" :visible.sync="dialogFormVisible">
    <el-form  label-width="100px">
  		<el-form-item label="新闻标题" >
			<el-input v-model="news.title" autocomplete="off"></el-input>
  		</el-form-item>
	  <el-form-item label="新闻内容" >
	  	<el-input v-model="news.content" type="textarea"  :rows="2" autocomplete="off"></el-input>
	  </el-form-item>
	  <el-form-item label="图片" >
	  	<!-- 已上传{{imagesList.length}}/1 -->
	  	<el-upload 
	  	  :action="apiPath+'/api/open/upload/upload'"
	  	  :headers="headers"
	  	  :show-file-list="false"
	  	  :on-preview="handlePictureCardPreview"
	  	  :on-success="handleAvatarSuccess"
	  	  :on-remove="handleRemove">
	  	  <i class="el-icon-plus" ></i>
	  	</el-upload>
	  	 <el-avatar size="large" :src="imagesList"></el-avatar>
	  </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="createNews">确 定</el-button>
    </div>
  </el-dialog>
  
  
  </d2-container>
</template>

<script>
  export default {
    data() {
      return {
		dialogFormVisible:false,
		activity:{},
		clubAdmin:y.getVal("userType"),
        tableData: [],
		apiPath:y.v.apiPath,
		dialogVisible:false,
		dialogImageUrl:'',
		headers:{
		  Authorization:y.getVal("jwt")
		},
		imagesList:'',
		news:{},
		page:{
		  size:10,
		  current:1,
		  total:0
		},
      }
    },
    methods: {
		deleteNews(id){
			var that=this
			y.confirm("提示","确定要删除吗？",function(){
				y.delete('/api/jwt/clubNews/clubNews?id='+id,{},function(data){
					if(data && data.success){
						that.$message.success("操作成功")
						that.getTableData()
					}else{
						that.$message.error(data.message)
					}
				})
			})
		},
		editNews(id,status){
			var that=this;
			y.put('/api/jwt/clubNews/clubNews?id='+id+"&status="+status,{},function(data){
				if(data && data.success){
					that.$message.success("操作成功")
					that.getTableData()
				}else{
					that.$message.error(data.message)
				}
			})
		},
		createNews(){
			if(!this.news.title){
				this.$message.error("请输入新闻标题")
				return false
			}
			if(!this.news.content){
				this.$message.error("请选择新闻内容")
				return false
			}
			if(!this.news.images){
				this.$message.error("请上传新闻图片")
				return false
			}
			var that=this
			y.post('/api/jwt/clubNews/clubNews',this.news,function(data){
				if(data && data.success){
					that.dialogFormVisible=false
					that.$message.success("创建成功")
					that.getTableData()
				}else{
					that.$message.error(data.message)
				}
			})
		},
		handleAvatarSuccess(res, file) {//上传头像
			var that=this;
			this.imagesList=require('../../../static/img/upload/'+res.data)
			this.news.images=res.data
		},
		handleRemove(file, fileList) {
			 //this.imagesList=fileList
		 },
		 handlePictureCardPreview(file) {
				this.dialogImageUrl = file;
				this.dialogVisible = true;
		 },
		changePage(value){//翻页
		  this.page.current=value
		  this.getTableData()
		},
		handleSizeChange(value){
		  this.page.size=value
		  this.getTableData()
		},
		getTableData(){
			var that=this
			y.get('/api/jwt/clubNews/clubNews?',this.page,function(data){
				if(data && data.success){
					that.tableData=data.data.records
					var len=data.data.records.length
					for(var i=0;i<len;i++){
						if(that.tableData[i].images){
							that.tableData[i].images=require("../../../static/img/upload/"+that.tableData[i].images)
						}
					}
					that.page.total=data.data.total
				}else{
					that.tableData=[]
				}
			})
		},
      indexMethod(index) {
        return index +1;
      }
    },
	created(){
		this.getTableData()
	}
  };
</script>