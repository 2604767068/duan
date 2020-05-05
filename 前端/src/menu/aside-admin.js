// 菜单 顶栏
//普通社团成员进来没有社团设置菜单,其他除了我的社团之外都只能看不能操作
export default [
  { path: '/adminIndex', title: '首页', icon: 'home' },
  {
    title: '社团管理',
    icon: 'fa fa-male',
	path:'/clubManager'
  },
  {
    title: '新闻咨询管理',
    icon: 'commenting-o',
	path:'/newsManager'
  },
  {
    title: '社团活动管理',
    icon: 'calendar',
  	path:'/clubActivityManager'
  },
  {
    title: '会员管理',
    icon: 'male',
	path:'/clubUserManager'
  },
  {
    title: '留言管理',
    icon: 'book',
	path:'/messageManager'
  },
  {
    title: '报表统计',
    icon: 'bar-chart-o',
	path:'/reportManager'
  },
]
