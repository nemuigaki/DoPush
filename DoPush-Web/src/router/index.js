import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
 roles: ['admin','editor']    control the page roles (you can set multiple roles)
 title: 'title'               the name show in sidebar and breadcrumb (recommend set)
 icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
 breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
 activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
 }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard' }
    }]
  },

  {
    path: '/channel',
    component: Layout,
    name: '渠道账号管理',
    meta: { title: '渠道账号管理', icon: 'channel' },
    children: [
      {
        path: 'create-channel',
        name: 'CreateChannel',
        component: () => import('@/views/channel/create.vue'),
        meta: { title: '新增渠道账号', icon: 'create' }
      },
      {
        path: 'list-channel',
        name: 'ListChannel',
        component: () => import('@/views/channel/list.vue'),
        meta: { title: '渠道账号列表', icon: 'nested' }
      }
    ]
  },

  {
    path: '/template',
    component: Layout,
    name: '消息模板管理',
    meta: { title: '消息模板管理', icon: 'template' },
    children: [
      {
        path: 'create-template',
        name: 'CreateTemplate',
        component: () => import('@/views/template/createOrUpdate.vue'),
        meta: { title: '新增消息模板', icon: 'create' }
      },
      {
        path: 'list-template',
        name: 'ListTemplate',
        component: () => import('@/views/template/list.vue'),
        meta: { title: '消息模板列表', icon: 'nested' }
      },
      {
        path: 'list-audit',
        name: 'ListAudit',
        component: () => import('@/views/template/auditList.vue'),
        meta: { title: '消息模板审核', icon: 'template' }
      }
    ]
  },

  {
    path: '/trace',
    component: Layout,
    name: '消息全链路追踪',
    meta: { title: '消息全链路追踪', icon: 'data-view' },
    children: [
      {
        path: 'user-trace',
        name: 'UserTrace',
        component: () => import('@/views/trace/user.vue'),
        meta: { title: '用户全链路追踪', icon: 'user' }
      },
      {
        path: 'template-trace',
        name: 'TemplateTrace',
        component: () => import('@/views/trace/template.vue'),
        meta: { title: '消息模板全链路追踪', icon: 'template' }
      },
      {
        path: 'sms-trace',
        name: 'SmsTrace',
        component: () => import('@/views/trace/sms.vue'),
        meta: { title: '短信发送数据查询', icon: 'nested' }
      },
      {
        path: 'offline-trace',
        name: 'OfflineTrace',
        component: () => import('@/views/trace/offline.vue'),
        meta: { title: '离线消息全链路追踪', icon: 'nested' }
      }
    ]
  },
  {
    path: '/material',
    component: Layout,
    name: '素材管理',
    meta: { title: '素材管理', icon: 'material' },
    children: [
      {
        path: 'index',
        name: 'Material',
        component: () => import('@/views/material/index.vue'),
        meta: { title: '素材市场', icon: 'link' }
      },
      {
        path: 'addOrUpdate',
        name: 'MaterialAddOrUpdate',
        component: () => import('@/views/material/add.vue'),
        meta: { title: '新增素材', icon: 'form' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    name: '用户管理',
    meta: { title: '用户管理', icon: 'user' },
    children: [
      {
        path: 'add',
        name: 'Add',
        component: () => import('@/views/user/add.vue'),
        meta: { title: '添加用户', icon: 'create' }
      },
      {
        path: 'permission',
        name: 'Permission',
        component: () => import('@/views/user/permission.vue'),
        meta: { title: '用户管理', icon: 'eye' }
      }
    ]
  },

  {
    path: '/export',
    component: Layout,
    name: '导出',
    meta: { title: '导出', icon: 'list' },
    children: [
      {
        path: 'exportFailedTask',
        name: 'ExportFailedTask',
        component: () => import('@/views/failedTask/index.vue'),
        meta: { title: '失败任务信息', icon: 'example' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
