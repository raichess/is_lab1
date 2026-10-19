<script setup>
import { reactive, ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { personApi } from '../api'
import { onPersonChanged } from '../live'
import EditDialog from '../components/EditDialog.vue'

const items = ref([])
const total = ref(0)
const page = ref(0)
const size = ref(10)
const sortBy = ref('id')
const sortDir = ref('asc')
const filters = reactive({ name: '', eyeColor: '', hairColor: '', nationality: '', locationName: '', heightMin: '', heightMax: '' })
const error = ref('')
const editing = ref(null)

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / size.value)))

const columns = [
  { key: 'id', label: 'ID', sort: 'id' },
  { key: 'name', label: 'Имя', sort: 'name' },
  { key: 'coordinates', label: 'Координаты' },
  { key: 'creationDate', label: 'Создан', sort: 'creationDate' },
  { key: 'eyeColor', label: 'Глаза', sort: 'eyeColor' },
  { key: 'hairColor', label: 'Волосы', sort: 'hairColor' },
  { key: 'location', label: 'Локация', sort: 'location.name' },
  { key: 'height', label: 'Рост', sort: 'height' },
  { key: 'nationality', label: 'Национальность', sort: 'nationality' },
]

async function load() {
  try {
    const data = await personApi.list({
      page: page.value,
      size: size.value,
      sort: `${sortBy.value},${sortDir.value}`,
      ...filters,
    })
    total.value = data.totalElements
    if (data.content.length === 0 && total.value > 0 && page.value > 0) {
      page.value = totalPages.value - 1
      return load()
    }
    items.value = data.content
    error.value = ''
  } catch (e) {
    error.value = e.message
  }
}

function sort(column) {
  if (sortBy.value === column) {
    sortDir.value = sortDir.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortBy.value = column
    sortDir.value = 'asc'
  }
}

function goTo(p) {
  page.value = p
  load()
}

function resetFilters() {
  Object.keys(filters).forEach((key) => (filters[key] = ''))
}

async function remove(person) {
  if (!confirm(`Удалить пипл #${person.id} «${person.name}»?`)) return
  try {
    await personApi.remove(person.id)
    await load()
  } catch (e) {
    error.value = e.message
  }
}

function onSaved() {
  editing.value = null
  load()
}

watch([sortBy, sortDir, size], () => {
  page.value = 0
  load()
})

let timer
watch(filters, () => {
  clearTimeout(timer)
  timer = setTimeout(() => {
    page.value = 0
    load()
  }, 300)
})

let off
onMounted(() => {
  load()
  off = onPersonChanged(() => load())
})
onUnmounted(() => {
  off?.()
  clearTimeout(timer)
})
</script>

<template>
  <h1>Список пипл</h1>
  <p v-if="error" class="banner error">{{ error }}</p>

  <div class="table-wrap">
    <table>
      <thead>
        <tr>
          <th
            v-for="c in columns"
            :key="c.key"
            :class="{ sortable: c.sort }"
            @click="c.sort && sort(c.sort)"
          >
            {{ c.label }}<span v-if="c.sort && c.sort === sortBy"> {{ sortDir === 'asc' ? '▲' : '▼' }}</span>
          </th>
          <th>Действия</th>
        </tr>
        <tr class="filters">
          <th></th>
          <th><input v-model="filters.name" placeholder="фильтр" /></th>
          <th></th>
          <th></th>
          <th><input v-model="filters.eyeColor" placeholder="фильтр" /></th>
          <th><input v-model="filters.hairColor" placeholder="фильтр" /></th>
          <th><input v-model="filters.locationName" placeholder="по названию" /></th>
          <th>
            <input v-model="filters.heightMin" type="number" step="any" placeholder="от" style="width: 4.5em" />
            <input v-model="filters.heightMax" type="number" step="any" placeholder="до" style="width: 4.5em" />
          </th>
          <th><input v-model="filters.nationality" placeholder="фильтр" /></th>
          <th><button class="secondary" @click="resetFilters">Сбросить</button></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="p in items" :key="p.id">
          <td>{{ p.id }}</td>
          <td>{{ p.name }}</td>
          <td>({{ p.coordinates.x }}; {{ p.coordinates.y }})</td>
          <td>{{ p.creationDate }}</td>
          <td>{{ p.eyeColor ?? '—' }}</td>
          <td>{{ p.hairColor }}</td>
          <td>{{ p.location.name ?? 'без названия' }} ({{ p.location.x }}; {{ p.location.y }}; {{ p.location.z }})</td>
          <td>{{ p.height }}</td>
          <td>{{ p.nationality ?? '—' }}</td>
          <td class="actions">
            <RouterLink :to="`/person/${p.id}`">Открыть</RouterLink>
            <button @click="editing = p">Изменить</button>
            <button class="danger" @click="remove(p)">Удалить</button>
          </td>
        </tr>
        <tr v-if="!items.length">
          <td colspan="10" class="empty">Нет объектов</td>
        </tr>
      </tbody>
    </table>
  </div>

  <div class="pager">
    <button :disabled="page === 0" @click="goTo(page - 1)">←</button>
    <span>Страница {{ page + 1 }} из {{ totalPages }} (всего {{ total }})</span>
    <button :disabled="page + 1 >= totalPages" @click="goTo(page + 1)">→</button>
    <select v-model.number="size">
      <option v-for="n in [5, 10, 20, 50]" :key="n" :value="n">{{ n }} на странице</option>
    </select>
  </div>

  <EditDialog v-if="editing" :person="editing" @close="editing = null" @saved="onSaved" />
</template>