<template>
	<d2-container>
		<el-card class="box-card" >
			<h2 style="float:left" type="primary">活动管理</h2>
		</el-card>
		<el-card class="box-card">
			按状态筛选：
			<el-button @click="searchByStatus()" :type="page.status==''?'primary':''">所有</el-button>
			<el-button @click="searchByStatus('0')" :type="page.status=='0'?'primary':''">正常</el-button>
			<el-button @click="searchByStatus('1')" :type="page.status=='1'?'primary':''">待审核</el-button>
			<el-button @click="searchByStatus('2')" :type="page.status=='2'?'primary':''">已驳回</el-button>
		</el-card>
  <el-table
    :data="tableData"
    border
    style="width: 100%">
	 <el-table-column type="expand">
	   <template slot-scope="props">
		   <p>活动时间：{{props.row.activityStartDate}}-{{props.row.activityEndDate}}</p>
		   <p>所需器材：{{props.row.tClubActivityAddress.material}}</p>
		   <p>活动场地：{{props.row.tClubActivityAddress.address}}</p>
	   </template>
	 </el-table-column>
	<el-table-column
	  type="index"
	  label="序号"
	  >
	</el-table-column>
	<el-table-column
	  label="状态"
	  width="120">
	  <template slot-scope="scope">
			  <el-tag v-if="scope.row.status==0" size="medium" type="success">正常</el-tag>
			   <el-tag v-if="scope.row.status==1" size="medium" type="info">待审核</el-tag>
				<el-tag v-if="scope.row.status==2" type="danger" size="medium">已拒绝</el-tag>
		</template>
	</el-table-column>
    <el-table-column
      prop="name"
      label="活动名称"
      width="150">
    </el-table-column>
	<el-table-column
	  prop="funds"
	  label="所需经费"
	  width="120">
	</el-table-column>
    <el-table-column
      prop="typeName"
      label="活动类型"
      width="120">
    </el-table-column>
    <el-table-column
      prop="tips"
      label="活动内容">
    </el-table-column>
    <el-table-column
      prop="createDate"
      label="发起时间"
      >
    </el-table-column>
	  <el-table-column
	    prop="reason"
	    label="驳回理由"
	    >
	</el-table-column>
	</el-table-column>
	  <el-table-column
	    prop="attence"
	    label="签到"
	    >
	</el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="100">
      <template slot-scope="scope">
        <el-button v-if="scope.row.status==1" type="text" size="small" @click="checkPass(scope.row.id,0)">审核</el-button>
		<el-button v-if="scope.row.status==1" type="text" size="small" @click="checkPass(scope.row.id,2)">驳回</el-button>
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
  	  <el-button type="primary" @click="sureClick(activityId,2)">确 定</el-button>
  	</div>
  </el-dialog>
  </d2-container>
</template>

<script>
  export default {
    data() {
      return {
        tableData: [],
		activityId:'',
		dialogTipsVisible:false,
		reason:'',
		page:{
		  size:10,
		  current:1,
		  total:0,
		  status:''
		},
      }
    },
	methods: {
		searchByStatus(status){
			if(status){
				this.page.status=status
			}else{
				this.page.status=''
			}
			this.getTableData()
		},
		checkPass(id,status){
			this.activityId=id
			if(status==2){
				this.dialogTipsVisible=true
				return
			}
			this.sureClick(id,status); 
		},
		sureClick(id,status){
			if(status==2){
			  if(!this.reason){
				  this.$message.error("请输入拒绝理由")
				  return false
			  }
			}
			var that=this
			y.post('/api/jwt/clubManager/activity?id='+id+'&status='+status+'&reason='+this.reason,{},function(data){
				if(data && data.success){
					that.dialogTipsVisible=false
					that.$message.success("操作成功")
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
	  	var that=this
	  	y.get('/api/jwt/clubManager/activity',this.page,function(data){
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
	},
	created(){
		this.getTableData()
	}
  }
</script>