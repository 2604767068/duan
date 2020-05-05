<template>
 <el-dropdown @command="handleCommand">
   <span class="el-dropdown-link">
     {{chooseClub}}<i class="el-icon-arrow-down el-icon--right"></i>
   </span>
   <el-dropdown-menu slot="dropdown">
     <el-dropdown-item v-for="(item,index) in tableData" :command="item.clubId+','+item.clubName+','+item.userType">{{item.clubName}}</el-dropdown-item>
   </el-dropdown-menu>
 </el-dropdown>
</template>

<script>
import { mapState, mapActions } from 'vuex'

export default {
  name: 'd2-header-color',
  data () {
    return {
      predefine: [
        '#ff4500',
        '#ff8c00',
        '#ffd700',
        '#90ee90',
        '#00ced1',
        '#1e90ff',
        '#c71585'
      ],
			tableData:[],
			chooseClub:''
    }
  },
  computed: {
    ...mapState('d2admin/color', [
      'value'
    ])
  },
  watch: {
    value (value) {
      this.set(value)
    }
  },
  methods: {
    ...mapActions('d2admin/color', [
      'set'
    ]),
		getMyClub(){
			var that=this
			var page={
				size:100,
				current:1
			}
			y.get('/api/jwt/user/clubs?status=0',page,function(data){
				if(data && data.success){
					that.tableData=data.data.records
					if(y.getVal(y.v.HearderParamClubId)){
						that.chooseClub=y.getVal("clubName")
						return
					}else{
						y.setVal(y.v.HearderParamClubId,data.data.records[0].clubId)
						y.setVal("clubName",data.data.records[0].clubName)
						y.setVal("userType",data.data.records[0].userType)
						that.chooseClub=data.data.records[0].clubName
					}
				}else{
					that.tableData=[]
				}
			})
		},
		handleCommand(value){
			this.$message.success('已切换社团'+value.split(',')[1])
			y.setVal(y.v.HearderParamClubId,value.split(',')[0])
			y.setVal("clubName",value.split(',')[1])
			y.setVal("userType",value.split(',')[2])
			this.chooseClub=value.split(',')[1]
			location.reload()
		}
  },
  created(){
	  this.getMyClub()
  }
}
</script>
