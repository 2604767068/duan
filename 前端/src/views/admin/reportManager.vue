<template>
  <d2-container>
    <template slot="header">经费记录</template>
    <div class="inner">
      <ve-line :data="chartData" :extend="extend" v-bind="pubSetting"></ve-line>
    </div>
  </d2-container>
</template>

<script>
export default {
  data () {
    return {
		pubSetting: {
		  height: '100%'
		},
      extend: {
        'xAxis.0.axisLabel.rotate': 45
      },
      chartData: {
        columns: ['日期', '经费'],
        rows: [
        ]
      }
    }
  },methods:{
		getReportData(){
			var that=this;
			y.get('/api/jwt/clubManager/fundsReport',{},function(data){
				if(data && data.success){
					that.chartData.rows=[]
					var len=data.data.length
					for(var i=0;i<len;i++){
						var row={
							'日期':data.data[i].date,
							'经费':data.data[i].funds
						}
						that.chartData.rows.push(row)
					}
					
				}
			})
		}
	},
	created(){
		this.getReportData()
	}
}
</script>

<style lang="scss" scoped>
.inner {
  position: absolute;
  top: 20px;
  right:  20px;
  bottom: 20px;
  left: 20px;
}
</style>
