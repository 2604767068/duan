<template>
	<d2-container>
		<el-card class="box-card" >
			<h2 style="float:left" type="primary">社团成员</h2>
		</el-card>
  <el-table
    :data="tableData"
    style="width: 100%">
    <el-table-column
      type="index"
      :index="indexMethod">
    </el-table-column>
    <el-table-column
      label="名称"
      >
	  <template slot-scope="scope">
		  <el-badge :value="'管理员'" class="item" v-if="scope.row.userType==1">
		  </el-badge>
	  		  <p  size="medium" type="success">{{scope.row.userName}}</p>
	  </template>
    </el-table-column>
	<el-table-column
	  label="备注名称"
	  >
	  <template slot-scope="scope">
		  <el-popover
		    placement="top"
		    width="160"
		    >
		    <el-input v-model="scope.row.aliasUserName"></el-input>
		    <div style="text-align: right; margin: 0">
		      <el-button size="mini" type="text" @click="scope.row.visible = false">取消</el-button>
		      <el-button type="primary" size="mini" @click="updateAlias(scope.row.id,scope.row.aliasUserName)">确定</el-button>
		    </div>
		    <p  slot="reference" style="color: #0074D9;">{{scope.row.aliasUserName}}</p>
		  </el-popover>
	  		  
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
      label="头像"
      width="180">
	  <template slot-scope="scope">
		  <el-avatar size="large" :src="scope.row.avatar"></el-avatar>
	  		  <!-- <img :src="scope.row.avatar"> -->
	  </template>
    </el-table-column>
    <el-table-column
      label="申请理由"
	  width="180">
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
	  prop="joinDate"
	  label="加入时间"
	  >
	</el-table-column>
	   <el-table-column
	      fixed="right"
	      label="操作"
	      width="100">
	      <template slot-scope="scope">
	        <el-button v-if="scope.row.status==1 && clubAdmin=='1'" @click="editClubFunction(scope.row,0)" type="text" size="small">同意</el-button>
			 <el-button v-if="scope.row.status==1 && clubAdmin=='1'" @click="editClubFunction(scope.row,2)" type="text" size="small">拒绝</el-button>
			<!-- <el-button v-if="scope.row.userType==1" type="text" size="small">解散</el-button> -->
			<el-button v-if="scope.row.status==0 && scope.row.userType!=1 && clubAdmin=='1'" @click="exitClub(scope.row.id)" type="text" size="small">踢出</el-button>
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
    <el-input v-model="tips" autocomplete="off"></el-input>
	<div slot="footer" class="dialog-footer">
	  <el-button @click="dialogTipsVisible = false">取 消</el-button>
	  <el-button type="primary" @click="joinClub(clubId,2)">确 定</el-button>
	</div>
  </el-dialog>
  </d2-container>
</template>

<script>
  export default {
    data() {
      return {
		  tips:'',//申请理由
		  dialogTipsVisible:false,
		  dialogVisible:false,
		  clubAdmin:y.getVal("userType"),
		  imagesList:'',
        tableData: [],
		visible:false,
		page:{
		  size:10,
		  current:1,
		  total:0
		},
		clubId:''
      }
    },
    methods: {
		editClubFunction(row,status){
			this.clubId=row.id
			if(status==2){
				this.dialogTipsVisible=true
				return false
			}
			this.joinClub(row.id,status)
		},
		joinClub(id,status){
			var that=this
			y.post('/api/jwt/clubUser/clubUser?id='+id+'&status='+status,{},function(data){
				if(data && data.success){
					that.$message.success("操作成功")
					that.dialogTipsVisible=false
					that.getTableData()
				}else{
					that.$message.error(data.message)
					that.dialogTipsVisible=false
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
		updateAlias(id,name){
			var that=this;
			var clubUser={
				id:id,
				aliasUserName:name
			}
			y.put('/api/jwt/clubUser/clubUser',clubUser,function(data){
				if(data && data.success){
					that.$message.success("修改成功")
					that.visible=false
					that.getTableData()
				}else{
					that.$message.error(data.message)
				}
			})
		},
		 exitClub(id){
			 var that=this;
			 y.confirm('提示',"确定踢出此人吗？",function(){
				 y.delete('/api/jwt/studentClub/club/'+id,{},function(data){
					if(data && data.success){
						that.$message.success("退出成功")
						that.getTableData()
					}else{
						that.$message.error(data.message)
					}
				 })
			 })
		
		 },
		getTableData(){
			var that=this
			y.get('/api/jwt/clubUser/clubUser',this.page,function(data){
				if(data && data.success){
					that.tableData=data.data.records
					var len=data.data.records.length
					for(var i=0;i<len;i++){
						if(that.tableData[i].userAvatar){
							that.tableData[i].userAvatar=require("../../../static/img/upload/"+that.tableData[i].userAvatar)
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