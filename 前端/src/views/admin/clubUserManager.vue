<template>
	<d2-container>
		<el-card class="box-card" >
			<h2 style="float:left" type="primary">会员管理</h2>
		</el-card>
		<el-card class="box-card" >
			<el-row>
				<el-col :span="8"><el-input v-model="page.name"></el-input></el-col>
				<el-button @click="getTableData()">搜索</el-button>
			</el-row>
			
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
	<el-table-column
	  label="名称"
	   prop="name"
	  >
	</el-table-column>
    <el-table-column
      prop="username"
      label="账号"
     >
    </el-table-column>
	<el-table-column
	  prop="email"
	  label="邮箱"
	 >
	</el-table-column>
    <el-table-column
      label="头像"
      width="120">
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
	<el-table-column
	  prop="createDate"
	  label="注册时间"
	  width="120">
	</el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="100">
      <template slot-scope="scope">
        <el-button type="text" size="small" @click="lookDetail(scope.row.id)">查看</el-button>
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
  <!-- 会员信息 -->
  <el-dialog title="会员信息" :visible.sync="dialogTableVisible" width="850px">
	  <el-card>
		  <div> <el-avatar size="large" :src="userInfo.avatar"></el-avatar></div>
		  <div> 注册时间:{{userInfo.createDate}}</div>
		    <div>姓名： {{userInfo.name}}</div>
			 <div> 账号：{{userInfo.username}}</div>
			  <div> 邮箱：{{userInfo.email}}</div>
			   <div>性别： <el-tag type="danger" v-if="userInfo.sex==0">女</el-tag>
			  <el-tag type="info" v-if="userInfo.sex==1">男</el-tag>
			  <el-tag  v-else>未知</el-tag></div>
			  <div v-if="userInfo.clubName">所在社团：{{userInfo.clubName.substring(0,userInfo.clubName.length-1)}}</div>
	  </el-card>
  </el-dialog>
  </d2-container>
</template>

<script>
  export default {
	  data() {
	    return {
			dialogTableVisible:false,
	      tableData: [],
		  page:{
		    size:10,
		    current:1,
		    total:0,
			name:''
		  },
		  userInfo:{}
	    }
	  },
    methods: {
	lookDetail(id){
		var that=this
		y.get('/api/jwt/clubManager/users/'+id,{},function(data){
			if(data && data.success){
				that.dialogTableVisible=true
				that.userInfo=data.data
				that.userInfo.avatar=require("../../../static/img/upload/"+that.userInfo.avatar)
			}else{
				that.userInfo={}
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
		y.get('/api/jwt/clubManager/users',this.page,function(data){
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
	}
    },
	created(){
		this.getTableData();
	},
   
  }
</script>