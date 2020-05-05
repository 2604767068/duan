<template>
	<d2-container>
		<el-card class="box-card" >
			<h2 style="float:left" type="primary">社团活动</h2>
			<el-button style="float:right;margin-left:3px" v-if="userType==1" type="primary" @click="dialogFormVisible=true">创建活动</el-button>
		</el-card>
		<el-row v-if="tableData.length>0">
		  <el-col  :span="8" v-for="(o, index) in tableData" :key="o.id" :offset="index > 0 ? 2 : 0">
		    <el-card :body-style="{ padding: '0px' }">
		     <div slot="header" class="clearfix" @click="getUserList(o.id)">
		         <el-badge :value="o.userNum" class="item">
				 <span>{{o.name}}</span>
				 </el-badge>
				 <el-tag v-if="o.status==0" size="medium" type="success">正常</el-tag>
				  <el-tag v-if="o.status==1" size="medium" type="info">待审核</el-tag>
				 <el-tag v-if="o.status==2" type="danger" size="medium">已拒绝</el-tag>
		         <el-button style="float: right; padding: 3px 0" type="text">{{o.typeName}}</el-button>
		       </div>
			  <div style="padding: 14px;">
		        <span></span>
		        <div class="bottom clearfix">
					<p>{{o.tips}}</p>
					<p>器材：<el-tag>{{o.tClubActivityAddress.material}}</el-tag></p>
					<p>场地：<el-tag type="danger">{{o.tClubActivityAddress.address}}</el-tag></p>
					<p v-if="o.reason">驳回理由：{{o.reason}}</p>
		          <p><time class="time">{{ o.activityStartDate }}-{{ o.activityEndDate }}</time></p>
				  <el-button v-if="o.inscroll==1 && o.status==0" type="danger" @click="joinActivity(o.id,1)" class="button">取消报名</el-button>
		          <el-button v-if="o.status==0 && !o.inscroll" type="text" @click="joinActivity(o.id,0)" class="button">申请报名</el-button>
				  <el-button v-if="userType==1" type="text" @click="deleteActivity(o.id)" class="button">删除活动</el-button>
				</div>
		      </div>
		    </el-card>
		  </el-col>
		</el-row>
		<el-row v-else>
			<el-divider>暂无数据</el-divider>
		</el-row>
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
  		<el-form-item label="活动名称" >
			<el-input v-model="activity.name" autocomplete="off"></el-input>
  		</el-form-item>
      <el-form-item label="活动类型" >
        <el-select v-model="activity.typeId" placeholder="请选择">
           <el-option
             v-for="item in typeList"
             :key="item.id"
             :label="item.name"
             :value="item.id">
           </el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="活动时间" >
           <el-date-picker
             v-model="datePicker"
             type="datetimerange"
			 value-format="yyyy-MM-dd HH:mm:ss"
             range-separator="至"
             start-placeholder="开始日期"
             end-placeholder="结束日期">
           </el-date-picker>
      </el-form-item>
	  <el-form-item label="活动内容" >
	  	<el-input v-model="activity.tips" type="textarea" :rows="2" autocomplete="off"></el-input>
	  </el-form-item>
	  <el-form-item label="所需经费" >
	  	<el-input v-model="activity.funds" type="number" autocomplete="off"></el-input>
	  </el-form-item>
	  <el-form-item label="所需器材" >
	  	<el-input v-model="activityAddress.material" autocomplete="off"></el-input>
	  </el-form-item>
	  <el-form-item label="活动场地" >
	  	<el-input v-model="activityAddress.address"  autocomplete="off"></el-input>
	  </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="createActivity">确 定</el-button>
    </div>
  </el-dialog>
  
  <!-- 报名者列表 -->
  <el-dialog title="报名列表" :visible.sync="dialogTableVisible" width="850px">
    <el-table :data="userList">
      <el-table-column property="name" label="名称" ></el-table-column>
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
  	   <el-table-column property="createDate" label="报名时间" ></el-table-column>
    </el-table>
  </el-dialog>
  
  </d2-container>
</template>

<script>
  export default {
    data() {
      return {
		dialogTableVisible:false,
		datePicker:[],
		dialogFormVisible:false,
		activity:{},
		activityAddress:{},
		typeList:[],
		userType:y.getVal("userType"),
        tableData: [],
		userList:[],
		page:{
		  size:10,
		  current:1,
		  total:0
		},
      }
    },
    methods: {
		deleteActivity(id){
			var that=this
			y.confirm("提示","确定要删除吗？",function(){
				y.delete('/api/jwt/clubActivity/activity?id='+id,{},function(data){
					if(data && data.success){
						that.$message.success("删除成功")
						that.getTableData()
					}else{
						that.$message.error(data.message)
					}
				})
			})
		},
		//申请报名
		joinActivity(id,status){
			var that=this
			var content="确定要报名吗？"
			if(status==1){
				content="确定要取消报名吗？"
			}
			y.confirm("提示",content,function(){
				y.post('/api/jwt/clubActivity/enroll?activityId='+id+'&status='+status,{},function(data){
					if(data && data.success){
						that.$message.success("操作成功")
						that.getTableData()
					}else{
						that.$message.error(data.message)
					}
				})
			})
		},
		getUserList(id){
			var that=this
			y.get('/api/jwt/clubActivity/enroll?activityId='+id,{},function(data){
				if(data && data.success){
					that.userList=data.data
				}else{
					that.userList=[]
				}
			})
			that.dialogTableVisible=true
		},
		createActivity(){
			if(!this.activity.name){
				this.$message.error("请输入活动名称")
				return false
			}
			if(!this.activity.typeId){
				this.$message.error("请选择活动类型")
				return false
			}
			if(this.datePicker.length==0){
				this.$message.error("请选择活动时间")
				return false
			}
			this.activity.activityStartDate=this.datePicker[0]
			this.activity.activityEndDate=this.datePicker[1]
			if(!this.activity.tips){
				this.$message.error("请输入活动内容")
				return false
			}
			if(!this.activity.funds){
				this.$message.error("请输入所需经费")
				return false
			}
			if(!this.activityAddress.material){
				this.$message.error("请输入所需器材")
				return false
			}
			if(!this.activityAddress.address){
				this.$message.error("请输入活动场地")
				return false
			}
                                              if(!this.activity.attence)
{
this.$message.error("请输入考勤");
return false
}
			var that=this
			this.activity.tClubActivityAddress=this.activityAddress
			y.post('/api/jwt/clubActivity/activity',this.activity,function(data){
				if(data && data.success){
					that.dialogFormVisible=false
					that.$message.success("创建成功")
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
			y.get('/api/jwt/clubActivity/activity?userId='+JSON.parse(y.getVal("userInfo")).id,this.page,function(data){
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
		getTypeList(){
			var that=this;
			y.get('/api/jwt/clubActivity/activityType',{},function(data){
				that.typeList=data.data
			})
		},
      indexMethod(index) {
        return index +1;
      }
    },
	created(){
		this.getTableData()
		this.getTypeList();
	}
  };
</script>