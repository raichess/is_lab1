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
const notice = ref('')
const deleting = ref(false)
let ownUntil = 0

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
  notice.value = ''
  const n = Number(idInput.value)
  if (!Number.isInteger(n)) {
    error.value = 'ID должен быть целым'
    return
  }
  if (String(n) === route.params.id) load()
  else router.push(`/person/${n}`)
}

async function remove() {
  deleting.value = true
  try {
    await personApi.remove(person.value.id)
    router.push('/')
  } catch (e) {
    error.value = e.message
  } finally {
    deleting.value = false
  }
}

function onSaved() {
  ownUntil = Date.now() + 1500
  editing.value = false
  load()
}

watch(() => route.params.id, () => {
  notice.value = ''
  load()
}, { immediate: true })

let off
onMounted(() => {
  off = onPersonChanged(async (message) => {
    if (!person.value || String(message.id) !== String(route.params.id)) return

    if (message.type === 'deleted') {
      if (deleting.value) return
      notice.value = `Пипл ${message.id} «${person.value.name}» был удалён в другом окне`
      person.value = null
      editing.value = false
      error.value = ''
    } else if (message.type === 'updated') {
      if (editing.value || Date.now() < ownUntil) return
      await load()
      if (person.value) notice.value = `Пипл ${message.id} «${person.value.name}» был изменён в другом окне`
    }
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
  <p v-if="notice" class="banner">
    {{ notice }}
  </p>

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