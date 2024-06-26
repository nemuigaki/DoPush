<template>
  <div class="app-container">
    <el-page-header content="消息模板列表" @back="goBack" />
    <el-divider />
    <el-input
      v-model="keywords"
      :placeholder="placeholder"
      prefix-icon="el-icon-search"
      style="width: 350px;"
      clearable
      @clear="search"
      @keydown.enter.native="search"
    />
    <el-button
      icon="el-icon-search"
      type="primary"
      @click="search"
    >
      搜索
    </el-button>
    <p />
    <el-button
      type="primary"
      @click="clearFilter"
    >
      清除所有过滤器
    </el-button>
    <el-table
      ref="table"
      :data="tableData"
      style="width: 100%"
      tooltip-effect="dark"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        v-for="item in columns"
        :key="item.name"
        :prop="item.name"
        :label="item.label"
        :type="item.typeLine"
        :filters="item.filters"
        :filter-method="item.filters ? filterTag : undefined"
      />
      <el-table-column label="操作">
        <template v-slot="scope">
          <div class="btns">
            <el-button
              v-if="scope.row['isDeleted'] === 0"
              class="btn"
              size="mini"
              @click="handleCheck(scope.$index, scope.row)"
            >查看</el-button>
            <br>
            <el-button
              v-if="scope.row['isDeleted'] === 0"
              class="btn"
              size="mini"
              type="success"
              @click="handlePass(scope.$index, scope.row)"
            >过审</el-button>
            <br>
            <el-button
              v-if="scope.row['isDeleted'] === 0"
              class="btn"
              size="mini"
              type="danger"
              @click="handleRefuse(scope.$index, scope.row)"
            >退回</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="currentPage"
      :page-sizes="pageSizes"
      :page-size="currentPerPage"
      layout="total, sizes, prev, pager, next, jumper"
      :total="count"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <el-dialog title="待审核模板查看" :visible.sync="checkTableVisible">
      <CreateOrUpdate :update="checkData" :operable="false" />
    </el-dialog>
  </div>
</template>

<script>
// eslint-disable-next-line no-unused-vars
import { auditList, auditPass, auditRefuse } from '@/api/template'
import { transTimestampToDate } from '@/utils/date'
import CreateOrUpdate from '@/views/template/createOrUpdate.vue'
// import createOrUpdate from '@/views/template/createOrUpdate.vue'

export default {
  components: { CreateOrUpdate },
  inject: ['reload'],
  // components: {
  //   CreateOrUpdate: createOrUpdate
  // },
  data() {
    return {
      keywords: '',
      placeholder: '通过关键字搜索',
      tableData: [],
      originData: [],
      channelType: '',
      pageSizes: [5, 10, 20],
      currentPage: 1,
      currentPerPage: 5,
      count: 0,
      selectedRows: [],
      checkTableVisible: false,
      checkData: '',
      currentOperateRowId: '',
      test: {
        receiver: '',
        msgContent: '',
        id: ''
      },
      sendChannelMap: {
        '20': 'PUSH通知栏',
        '30': '短信',
        '40': '邮箱',
        '50': '微信服务号（模板消息）',
        '60': '微信小程序（订阅消息）',
        '70': '企业微信（应用消息）',
        '80': '钉钉群机器人',
        '90': '钉钉工作消息',
        '100': '企业微信（机器人）',
        '110': '飞书机器人'
      },
      templateTypeMap: {
        '10': '定时任务发送',
        '20': '实时调用接口'
      },
      msgTypeMap: {
        '10': '通知类',
        '20': '营销类',
        '30': '验证码'
      },
      idTypeMap: {
        '10': '用户ID',
        '20': '设备号',
        '30': '手机号',
        '40': 'openId',
        '50': '邮箱地址',
        '60': '企业微信userId',
        '70': '钉钉userId',
        '80': '推送通知栏cid',
        '90': '飞书userId'
      },
      auditStatusMap: {
        '10': '待审核',
        '20': '审核成功',
        '30': '被拒绝'
      },
      columns: [
        {
          typeLine: 'selection'
        },
        {
          'name': 'id',
          'label': 'ID',
          'width': 20,
          'sortable': true
        },
        {
          'name': 'name',
          'label': '模板消息名'
        },
        {
          'name': 'sendChannel',
          'label': '发送渠道',
          filters: [
            { text: 'PUSH通知栏', value: 'PUSH通知栏' },
            { text: '短信', value: '短信' },
            { text: '邮箱', value: '邮箱' },
            { text: '微信服务号（模板消息）', value: '微信服务号（模板消息）' },
            { text: '微信小程序（订阅消息）', value: '微信小程序（订阅消息）' },
            { text: '钉钉群机器人', value: '钉钉群机器人' },
            { text: '钉钉工作消息', value: '钉钉工作消息' },
            { text: '企业微信（机器人）', value: '企业微信（机器人）' },
            { text: '飞书机器人', value: '飞书机器人' }
          ]
        },
        {
          'name': 'templateType',
          'label': '模板类型',
          filters: [
            { text: '定时任务发送', value: '定时任务发送' },
            { text: '实时调用接口', value: '实时调用接口' }
          ]
        },
        {
          'name': 'msgType',
          'label': '消息类型',
          filters: [
            { text: '通知类', value: '通知类' },
            { text: '营销类', value: '营销类' },
            { text: '验证码', value: '验证码' }
          ]
        },
        {
          'name': 'creator',
          'label': '创建者'
        },
        {
          'name': 'idType',
          'label': '接收者ID类型'
        },
        {
          'name': 'created',
          'label': '创建时间'
        },
        {
          'name': 'updated',
          'label': '修改时间'
        },
        {
          'name': 'auditStatus',
          'label': '审核状态',
          filters: [
            { text: '待审核', value: '待审核' },
            { text: '审核成功', value: '审核成功' },
            { text: '被拒绝', value: '被拒绝' }
          ]
        }
      ]
    }
  },
  mounted() {
    this.search()
    this.$bus.$on('templateUpdate', () => {
      this.reload()
    })
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    search() {
      const data = {}
      data.keywords = this.keywords
      data.creator = 'Sadalsuud'
      data.page = this.currentPage
      data.perPage = this.currentPerPage
      auditList(data).then(res => {
        const rows = res.data.rows
        this.originData = rows
        // this.tableData = this.dataConvert(rows)
        // 解决原始数据被修改无法使用的问题, 采用序列化方式实现数据的深拷贝
        this.tableData = this.dataConvert(JSON.parse(JSON.stringify(rows)))
        this.count = res.data.count
      }).catch(err => {
        console.log(err)
      })
      // console.log(this.keyword)
    },
    filterTag(value, row) {
      return row.sendChannel === value ||
        row.templateType === value ||
        row.msgType === value ||
        row.auditStatus === value
    },
    clearFilter() {
      this.$refs.table.clearFilter()
    },
    handleSelectionChange(val) {
      this.selectedRows = val
    },
    dataConvert(datas) {
      for (let i = 0; i < datas.length; i++) {
        const data = datas[i]
        data.sendChannel = this.sendChannelMap[data.sendChannel]
        data.templateType = this.templateTypeMap[data.templateType]
        data.msgType = this.msgTypeMap[data.msgType]
        data.idType = this.idTypeMap[data.idType]
        data.auditStatus = this.auditStatusMap[data.auditStatus]
        data.created = transTimestampToDate(data.created)
        data.updated = transTimestampToDate(data.updated)
      }
      return datas
    },
    handleSizeChange(val) {
      this.currentPerPage = val
      this.search()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.search()
    },
    handleCheck(index, row) {
      console.log(row)
      this.checkData = this.originData.filter(item => {
        if (item.id === row.id) {
          return item
        }
      })[0]
      if (this.updateData === null) {
        this.$message.error('数据错误')
        return
      }
      const msgContent = this.checkData.msgContent
      Object.assign(this.checkData, JSON.parse(msgContent))
      console.log(this.checkData)
      this.checkTableVisible = true
    },
    handlePass(index, row) {
      console.log(index)
      const template = this.originData[index]
      auditPass(template, true).then(res => {
        if (res.status === '200') {
          this.$message.success(res.msg)
          this.search()
          return
        }
        this.$message.error(res.msg)
      })
    },
    handleRefuse(index, row) {
      console.log(index)
      const template = this.originData[index]
      auditRefuse(template, true).then(res => {
        if (res.status === '200') {
          this.$message.success(res.msg)
          this.search()
          return
        }
        this.$message.error(res.msg)
      })
    }
  }
}
</script>

<style scoped>
.btns {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}
.btn {
  width: 67%;
}
</style>

