import { createRouter, createWebHashHistory } from 'vue-router'
import PersonsTable from './views/PersonsTable.vue'
import PersonCreate from './views/PersonCreate.vue'
import PersonView from './views/PersonView.vue'
import References from './views/References.vue'
import SpecialOps from './views/SpecialOps.vue'
import PersonEdit from './views/PersonEdit.vue'
import PersonDelete from './views/PersonDelete.vue'

export default createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', component: PersonsTable },
    { path: '/create', component: PersonCreate },
    { path: '/person/:id?', component: PersonView },
    { path: '/references', component: References },
    { path: '/special', component: SpecialOps },
    { path: '/edit', component: PersonEdit },
    { path: '/delete', component: PersonDelete },

  ],
})
