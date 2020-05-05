 <template>
	<d2-container>
		<el-card class="box-card" >
			<h2 style="float:left" type="primary">我加入的社团</h2>
			<el-button style="float:right;margin-left:3px" type="primary" @click="getClubList()">申请加入</el-button>
			<el-button style="float:right" type="success" @click="dialogFormVisible=true,newClub={}">创建社团</el-button>
		</el-card>
  <el-table
    :data="tableData"
    style="width: 100%">
    <el-table-column
      type="index"
      :index="indexMethod">
    </el-table-column>
	<el-table-column
	  label="社团状态"
	  >
	  <template slot-scope="scope">
		  <el-tag v-if="scope.row.clubStatus==0" size="medium" type="success">正常</el-tag>
		   <el-tag v-if="scope.row.clubStatus==1" size="medium" type="info">待审核</el-tag>
		  <el-tag v-if="scope.row.clubStatus==2" type="danger" size="medium">已拒绝</el-tag>
	  </template>
	</el-table-column>
    <el-table-column
      label="名称"
      >
	  <template slot-scope="scope">
		  <el-badge :value="'管理员'" class="item" v-if="scope.row.userType==1">
		  </el-badge>
	  		  <p  size="medium" type="success">{{scope.row.clubName}}</p>
	  </template>
    </el-table-column>
	<el-table-column
	  label="状态"
	  >
	  <template slot-scope="scope">
		  <el-tag v-if="scope.row.status==0" size="medium" type="success">正常</el-tag>
		   <el-tag v-if="scope.row.status==1" size="medium" type="info">待审核</el-tag>
		  <el-tag v-if="scope.row.status==2" type="danger" size="medium">已拒绝</el-tag>
	  </template>
	</el-table-column>
    <el-table-column
      label="logo"
      width="180">
	  <template slot-scope="scope">
		  <el-avatar size="large" :src="scope.row.avatar"></el-avatar>
	  		  <!-- <img :src="scope.row.avatar"> -->
	  </template>
    </el-table-column>
    <el-table-column
      label="说明信息"
	  width="180">
	     <template slot-scope="scope">
	          <el-popover trigger="hover" placement="top">
	            <p>{{ scope.row.clubTips }}</p>
	            <div slot="reference" class="name-wrapper">
	              <el-tag size="medium">{{ scope.row.clubTips }}</el-tag>
	            </div>
	          </el-popover>
	        </template>
    </el-table-column>
	<el-table-column
	  prop="joinDate"
	  label="加入时间"
	  >
	</el-table-column>
	<el-table-column
	  prop="clubReason"
	  label="驳回理由"
	  >
	</el-table-column>
	   <el-table-column
	      fixed="right"
	      label="操作"
	      width="100">
	      <template slot-scope="scope">
	        <el-button v-if="scope.row.userType==1" @click="editClubFunction(scope.row)" type="text" size="small">编辑</el-button>
			 <el-button v-if="scope.row.clubStatus==2 && scope.row.userType==1" @click="clubApply(scope.row.clubId)" type="text" size="small">重新申请</el-button>
			 <el-button v-if="scope.row.status==2" @click="userApply(scope.row.id)" type="text" size="small">重新申请</el-button>
			
			<!-- <el-button v-if="scope.row.userType==1" type="text" size="small">解散</el-button> -->
			<el-button v-if="scope.row.status==0 && scope.row.userType!=1" @click="exitClub(scope.row.id)" type="text" size="small">退出</el-button>
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
  <el-dialog title="创建社团" :visible.sync="dialogFormVisible">
    <el-form  label-width="100px">
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
      <el-form-item label="社团名称" >
        <el-input v-model="newClub.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="社团说明" >
        <el-input v-model="newClub.tips" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="createClub">确 定</el-button>
    </div>
  </el-dialog>
  
  
  <el-dialog :visible.sync="dialogVisible">
    <img width="100%" :src="dialogImageUrl" alt="">
  </el-dialog>
  <!-- 申请加入社团列表 -->
  <el-dialog title="社团列表" :visible.sync="dialogTableVisible" width="850px">
    <el-table :data="clubList">
      <el-table-column property="name" label="名称" ></el-table-column>
	  <el-table-column property="name" label="logo" >
		  <template slot-scope="scope">
		  		  <el-avatar size="large" :src="scope.row.avatar"></el-avatar>
		  </template>
	  </el-table-column>
	  <el-table-column property="userName" label="管理员">
	  </el-table-column>
      <el-table-column  label="管理员头像" width="200">
		  <template slot-scope="scope">
		  		  <el-avatar size="large" :src="scope.row.userAvatar"></el-avatar>
		  </template>
	  </el-table-column>
      <el-table-column property="tips" label="说明" width="100">
		  <template slot-scope="scope">
		       <el-popover trigger="hover" placement="top">
		         <p>{{ scope.row.tips }}</p>
		         <div slot="reference" class="name-wrapper">
		           <el-tag size="medium">{{ scope.row.tips }}</el-tag>
		         </div>
		       </el-popover>
		     </template>
	  </el-table-column>
	   <el-table-column property="createDate" label="创建时间" ></el-table-column>
	      <el-table-column
	         fixed="right"
	         label="操作"
	         width="100">
	         <template slot-scope="scope">
	           <el-button @click="clubId=scope.row.id,dialogTipsVisible=true" type="text" size="small">申请加入</el-button>
	         </template>
	       </el-table-column>
    </el-table>
	<el-pagination
	  style="float: right;"
	  :current-page="page1.current"
	  :page-size="page1.size"
	  :page-sizes="[10, 20, 50, 100]"
	   @size-change="handleSizeChange1"
	   layout="sizes,prev,pager,next"
	  @current-change="changePage1"
	  :total="page1.total">
	</el-pagination>
  </el-dialog>
  <el-dialog title="申请理由" :visible.sync="dialogTipsVisible">
    <el-input v-model="tips" autocomplete="off"></el-input>
	<div slot="footer" class="dialog-footer">
	  <el-button @click="dialogTipsVisible = false">取 消</el-button>
	  <el-button type="primary" @click="joinClub()">确 定</el-button>
	</div>
  </el-dialog>
  </d2-container>
</template>

<script>
  export default {
    data() {
      return {
		  tips:'',//申请理由
		  dialogTableVisible:false,
		  dialogTipsVisible:false,
		  apiPath:y.v.apiPath,
		  dialogVisible:false,
		  dialogImageUrl:'',
		  headers:{
		    Authorization:y.getVal("jwt")
		  },
		  imagesList:'',
		  newClub:{},
		 dialogFormVisible:false,
        tableData: [],
		page:{
		  size:10,
		  current:1,
		  total:0
		},
		page1:{
		  size:10,
		  current:1,
		  total:0
		},
		clubList:[],
		clubId:''
      }
    },
    methods: {
		clubApply(id){
			var that=this
			y.put('/api/jwt/studentClub/clubApply?clubId='+id,{},function(data){
				if(data && data.success){
					that.$message.success("申请成功")
					that.getTableData()
				}else{
					that.$message.error(data.message)
				}
			})
		},
		userApply(id){
			var that=this
			y.put('/api/jwt/studentClub/clubUser?id='+id,{},function(data){
				if(data && data.success){
					that.$message.success("申请成功")
					that.getTableData()
				}else{
					that.$message.error(data.message)
				}
			})
		},
		editClubFunction(club){
			var that=this;
			y.get('/api/jwt/studentClub/club/'+club.clubId,{},function(data){
				if(data && data.success){
					that.newClub=data.data
					that.imagesList=require('../../../static/img/upload/'+data.data.avatar)
					that.dialogFormVisible=true
				}
			})
		},
		joinClub(){
			if(!this.tips){
				this.$message.error("请输入申请理由")
				return false
			}
			var clubUser={
				clubId:this.clubId,
				tips:this.tips
			}
			var that=this;
			y.post('/api/jwt/studentClub/join',clubUser,function(data){
				if(data && data.success){
					that.$message.success("申请成功")
					that.dialogTipsVisible=false
					that.getTableData()
				}else if(data && data.data.status=='1'){
					that.$message.error("已在申请中")
					that.dialogTipsVisible=false
				}else if(data && data.data.status=='0'){
					that.$message.error("已在该社团中")
					that.dialogTipsVisible=false
				}
			})
		},
		getClubList(){
			this.dialogTableVisible=true
			var that=this;
			y.get('/api/jwt/studentClub/club?status=0',this.page1,function(data){
				if(data && data.success){
					that.clubList=data.data.records
					var len=data.data.records.length
					for(var i=0;i<len;i++){
						if(that.clubList[i].avatar){
							that.clubList[i].avatar=require("../../../static/img/upload/"+that.clubList[i].avatar)
						}
						if(that.clubList[i].userAvatar){
							that.clubList[i].userAvatar=require("../../../static/img/upload/"+that.clubList[i].userAvatar)
						}
					}
					that.page1.total=data.data.total
				}else{
					that.clubList=[]
				}
			})
		},
		handleAvatarSuccess(res, file) {//上传头像
			var that=this;
			// var img={
			// 	name:'1',
			// 	url:'../../../static/img/upload/'+res.data
			// }
			this.imagesList=require('../../../static/img/upload/'+res.data)
			this.newClub.avatar=res.data
		},
		handleRemove(file, fileList) {
			 //this.imagesList=fileList
		 },
		 handlePictureCardPreview(file) {
				this.dialogImageUrl = file;
				this.dialogVisible = true;
		 },
		 exitClub(id){
			 var that=this;
			 y.del('/api/jwt/studentClub/club/'+id,{},function(data){
				if(data && data.success){
					that.$message.success("退出成功")
					that.getTableData()
				}else{
					that.$message.error(data.message)
				}
			 })
		 },
		createClub(){
			// if(this.imagesList.length>0){
			// 	this.newClub.avatar=this.imagesList.join(",")
			// }
			var that=this;
			if(this.newClub.id){//有ID为修改
				y.put('/api/jwt/studentClub/club',this.newClub,function(data){
					if(data && data.success){
						that.$message.success("修改成功")
						that.dialogFormVisible=false
						that.getTableData()
					}else{
						that.$message.error(data.message)
					}
				})
			}else{
				y.post('/api/jwt/studentClub/club',this.newClub,function(data){
					if(data && data.success){
						that.$message.success("注册成功")
						that.dialogFormVisible=false
						that.getTableData()
					}else{
						that.$message.error(data.message)
					}
				})
			}
		},
		changePage(value){//翻页
		  this.page.current=value
		  this.getTableData()
		},
		handleSizeChange(value){
		  this.page.size=value
		  this.getTableData()
		},
		changePage1(value){//翻页
		  this.page1.current=value
		  this.getClubList()
		},
		handleSizeChange1(value){
		  this.page1.size=value
		  this.getClubList()
		},
		getTableData(){
			var that=this
			y.get('/api/jwt/user/clubs',this.page,function(data){
				if(data && data.success){
					that.tableData=data.data.records
					var len=data.data.records.length
					for(var i=0;i<len;i++){
						if(that.tableData[i].avatar){
							that.tableData[i].avatar=require("../../../static/img/upload/"+that.tableData[i].avatar)
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
		//this.src=require("../../../static/img/upload/微信图片_20200208211731.jpg")
		this.getTableData()
	}
  };
</script>