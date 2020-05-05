// 菜单 顶栏
//普通社团成员进来没有社团设置菜单,其他除了我的社团之外都只能看不能操作
export default [
  { path: '/index', title: '首页', icon: 'home' },
  {
    title: '我的社团',
    icon: 'fa fa-male',
	path:'/myClub'
  },
  {
    title: '社团成员',
    icon: 'male',
	path:'/clubUser'
  },
  {
    title: '社团活动',
    icon: 'calendar',
  	path:'/clubActivity'
  },
  {
    title: '社团设置',
    icon: 'cog',
	path:'/clubSetting'
  },
  {
    title: '留言管理',
    icon: 'book',
	path:'/clubMessage'
  },
]
