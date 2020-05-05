<template>
	<d2-container>
		<el-card class="box-card" >
			<h2 style="float:left" type="primary">社团管理</h2>
			<el-button style="float:right;margin-left:3px"  type="primary" @click="dialogFormVisible=true">社团设置</el-button>
		</el-card>
  <el-table
    :data="tableData"
    border
    style="width: 100%">
	<el-table-column
	  type="index"
	  label="序号"
	  >
	</el-table-column>
	<el-table-column property="name" label="名称" ></el-table-column>
	<el-table-column property="name" label="logo" >
			  <template slot-scope="scope">
			  		  <el-avatar size="large" :src="scope.row.avatar"></el-avatar>
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
	<el-table-column
	  prop="createDate"
	  label="创建时间"
	  >
	</el-table-column>
	<el-table-column property="createDate" label="创建时间" ></el-table-column>
	   <el-table-column
	      fixed="right"
	      label="操作"
	      width="100">
	      <template slot-scope="scope">
	        <el-button v-if="scope.row.status==1" @click="updateStatus(scope.row.id,0)" type="text" size="small">同意</el-button>
			<el-button v-if="scope.row.status==1" @click="updateStatus(scope.row.id,2)" type="text" size="small">拒绝</el-button>
			<el-button v-if="scope.row.status==0" @click="getClubNum(scope.row.id)" type="text" size="small">查看人数</el-button>
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
	<el-dialog title="拒绝理由" :visible.sync="dialogTipsVisible">
	  <el-input v-model="reason" autocomplete="off"></el-input>
		<div slot="footer" class="dialog-footer">
		  <el-button @click="dialogTipsVisible = false">取 消</el-button>
		  <el-button type="primary" @click="toUpdate(clubId,2)">确 定</el-button>
		</div>
	</el-dialog>
	
	<el-dialog title="社团设置" :visible.sync="dialogFormVisible">
	  每人创建最大社团数<el-input v-model="setting.num" autocomplete="off"></el-input>
		<div slot="footer" class="dialog-footer">
		  <el-button @click="dialogFormVisible = false">取 消</el-button>
		  <el-button type="primary" @click="updateSetting()">确 定</el-button>
		</div>
	</el-dialog>
	
	<!-- 社团成员 -->
	<el-dialog title="成员列表" :visible.sync="dialogTableVisible" width="850px">
	  <el-table :data="userList">
	   <el-table-column
	     label="名称"
	     >
	     <template slot-scope="scope">
	   	  <el-badge :value="'管理员'" class="item" v-if="scope.row.userType==1">
	   	  </el-badge>
	     		  <p  size="medium" type="success">{{scope.row.name}}</p>
	     </template>
	   </el-table-column>
		  <el-table-column property="name" label="头像" >
			  <template slot-scope="scope">
			  		  <el-avatar size="large" :src="scope.row.avatar"></el-avatar>
			  </template>
		  </el-table-column>
	    <el-table-column  label="性别" width="200">
			  <template slot-scope="scope">
				  <el-tag type="danger" v-if="scope.row.sex==0">女</el-tag>
				  <el-tag type="info" v-if="scope.row.sex==1">男</el-tag>
				  <el-tag  v-else>未知</el-tag>
			  </template>
		  </el-table-column>
		  <el-table-column  label="邮箱" prop="email" width="200">
		    </el-table-column>
			   <el-table-column
			      fixed="right"
			      label="操作"
			      width="100">
			      <template slot-scope="scope">
					<el-button  @click="updateAdmin(scope.row.id)" type="text" size="small">设为社团负责人</el-button>
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
  </d2-container>
</template>

<script>
  export default {

    data() {
      return {
		  setting:{},
		  dialogFormVisible:false,
        tableData: [],
		dialogTableVisible:false,
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
		dialogTipsVisible:false,
		reason:'',
		userList:[],
		clubId:'',
      }
    },
    methods: {
		getSetting(){
			var that=this
			y.get('/api/jwt/clubManager/setting',{},function(data){
				that.setting=data.data
			})
		},
		updateSetting(){
			var that=this
			if(this.setting.num<3){
				 that.$message.error("最小不能少许3")
				return false
			}
			y.put('/api/jwt/clubManager/setting?id='+this.setting.id+'&num='+this.setting.num,{},function(data){
				if(data && data.success){
						that.$message.success("操作成功")
						that.dialogFormVisible=false
					}else{
					   that.$message.error(data.message)
				}
			})
		},
		changePage1(value){//翻页
		  this.page1.current=value
		  this.getClubNum(this.clubId)
		},
		handleSizeChange1(value){
		  this.page1.size=value
		  this.getClubNum(this.clubId)
		},
		updateAdmin(id){//修改社团负责人
			var that=this
			y.confirm("提示","确定要修改社团负责人吗？",function(){
				y.post('/api/jwt/clubManager/clubAdmin?clubId='+that.clubId+'&userId='+id,{},function(data){
					if(data && data.success){
						that.$message.success("操作成功")
						that.dialogTableVisible=false
						that.getTableData()
					}else{
					   that.$message.error(data.message)
				   }
				})
			})
		},
	  getClubNum(id){
		  var that=this;
		  this.clubId=id
		   y.get('/api/jwt/clubManager/clubUsers?clubId='+id,this.page1,function(data){
			  if(data && data.success){
			  		that.userList=data.data.records
					var len=data.data.records.length
					for(var i=0;i<len;i++){
						if(that.userList[i].avatar){
							that.userList[i].avatar=require("../../../static/img/upload/"+that.userList[i].avatar)
						}
					}
					that.page1.total=data.data.total
			  	}else{
			  		that.userList=[]
			  	}
			  })
			  that.dialogTableVisible=true
	  },
	  updateStatus(id,status){
		this.clubId=id
		if(status==2){
			this.dialogTipsVisible=true
			return
		}
		this.toUpdate(id,status);  
	  },
	  toUpdate(id,status){
		  if(status==2){
			  if(!this.reason){
				  this.$message.error("请输入拒绝理由")
				  return false
			  }
		  }
		  var that=this;
		   y.post('/api/jwt/clubManager/club?id='+id+'&status='+status+"&reason="+this.reason,{},function(data){
			   if(data && data.success){
				   that.$message.success("操作成功")
				   that.dialogTipsVisible=false
				   that.getTableData()
			   }else{
				   that.$message.error(data.message)
			   }
		   })
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
			var that=this;
			y.get('/api/jwt/studentClub/club',this.page,function(data){
				if(data && data.success){
					that.tableData=data.data.records
					var len=data.data.records.length
					for(var i=0;i<len;i++){
						if(that.tableData[i].avatar){
							that.tableData[i].avatar=require("../../../static/img/upload/"+that.tableData[i].avatar)
						}
						if(that.tableData[i].userAvatar){
							that.tableData[i].userAvatar=require("../../../static/img/upload/"+that.tableData[i].userAvatar)
						}
					}
					that.page.total=data.data.total
				}else{
					that.tableData=[]
				}
			})
		}
    },
		created(){
			this.getTableData()
			this.getSetting();
		}
  }
</script>