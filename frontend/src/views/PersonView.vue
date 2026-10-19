<script setup>
import { ref, watch, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { personApi } from '../api'
import { onPersonChanged } from '../live'
import EditDialog from '../components/EditDialog.vue'

const route = useRoute()
const router = useRouter()

const idInput = ref('')
const person = ref(null)
const error = ref('')
const editing = ref(false)

async function load() {
  const id = route.params.id
  idInput.value = id ?? ''
  if (!id) {
    person.value = null
    error.value = ''
    return
  }
  try {
    person.value = await personApi.get(id)
    error.value = ''
  } catch (e) {
    person.value = null
    error.value = e.message
  }
}

function search() {
  const n = Number(idInput.value)
  if (!Number.isInteger(n) || n <= 0) {
    error.value = 'ID должен быть целым и больше 0!'
    return
  }
  if (String(n) === route.params.id) load()
  else router.push(`/person/${n}`)
}

async function remove() {
  if (!confirm(`Удалить пиил#${person.value.id} «${person.value.name}»?`)) return
  try {
    await personApi.remove(person.value.id)
    router.push('/')
  } catch (e) {
    error.value = e.message
  }
}

function onSaved() {
  editing.value = false
  load()
}

watch(() => route.params.id, load, { immediate: true })

let off
onMounted(() => {
  off = onPersonChanged((message) => {
    if (String(message.id) === String(route.params.id)) load()
  })
})
onUnmounted(() => off?.())
</script>

<template>
  <h1>Информация </h1>

  <form class="inline" @submit.prevent="search">
    <input v-model="idInput" type="number" min="1" step="1" placeholder="ID объекта" />
    <button type="submit">Найти</button>
  </form>

  <p v-if="error" class="banner error">{{ error }}</p>

  <section v-if="person" class="card">
    <h2>{{ person.name }} <small>#{{ person.id }}</small></h2>
    <dl>
      <dt>Дата создания</dt>
      <dd>{{ person.creationDate }}</dd>
      <dt>Цвет глаз</dt>
      <dd>{{ person.eyeColor ?? '—' }}</dd>
      <dt>Цвет волос</dt>
      <dd>{{ person.hairColor }}</dd>
      <dt>Рост</dt>
      <dd>{{ person.height }}</dd>
      <dt>Национальность</dt>
      <dd>{{ person.nationality ?? '—' }}</dd>
    </dl>

    <h3>Связанные объекты</h3>
    <div class="related">
      <div class="card">
        <h4>Координаты {{ person.coordinates.id }}</h4>
        <p>x = {{ person.coordinates.x }}</p>
        <p>y = {{ person.coordinates.y }}</p>
      </div>
      <div class="card">
        <h4>Локация {{ person.location.id }}</h4>
        <p>Название {{ person.location.name ?? '—' }}</p>
        <p>x = {{ person.location.x }}, y = {{ person.location.y }}, z = {{ person.location.z }}</p>
      </div>
    </div>

    <div class="actions">
      <button @click="editing = true">Изменить</button>
      <button class="danger" @click="remove">Удалить</button>
    </div>
  </section>

  <EditDialog v-if="editing && person" :person="person" @close="editing = false" @saved="onSaved" />
</template>
