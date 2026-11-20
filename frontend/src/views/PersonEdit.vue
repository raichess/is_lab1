<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { personApi } from '../api'
import { onPersonChanged } from '../live'
import PersonForm from '../components/PersonForm.vue'

const router = useRouter()

const idInput = ref('')
const person = ref(null)
const error = ref('')
const info = ref('')
const outdated = ref(false)
const formKey = ref(0)

function markDeleted(id) {
  person.value = null
  outdated.value = false
  error.value = ''
  info.value = `Пипл ${id} был удалён в другом окне, редактировать нечего`
}

async function findPerson() {
  error.value = ''
  info.value = ''
  outdated.value = false
  person.value = null

  const id = Number(idInput.value)
  if (!Number.isInteger(id)) {
    error.value = 'ID должен быть целым числом'
    return
  }

  try {
    person.value = await personApi.get(id)
    formKey.value++
  } catch (e) {
    error.value = e.message
  }
}

async function reload() {
  if (!person.value) return
  const id = person.value.id
  try {
    person.value = await personApi.get(id)
    formKey.value++
    outdated.value = false
    info.value = ''
  } catch (e) {
    if (e.status === 404) markDeleted(id)
    else error.value = e.message
  }
}

function onSaved(savedPerson) {
  router.push(`/person/${savedPerson.id}`)
}

let off
onMounted(() => {
  off = onPersonChanged((message) => {
    if (!person.value || String(message.id) !== String(person.value.id)) return

    if (message.type === 'deleted') {
      markDeleted(message.id)
    } else if (message.type === 'updated') {
      outdated.value = true
      info.value = `Пипл ${message.id} был изменён в другом окне`
    }
  })
})
onUnmounted(() => off?.())
</script>

<template>
  <h1>Апдейт</h1>

  <form class="inline search-form" @submit.prevent="findPerson">
    <input
        v-model="idInput"
        type="number"
        min="1"
        step="1"
        placeholder="ID человека"
    />
    <button type="submit">Найти</button>
  </form>

  <p v-if="error" class="banner error">{{ error }}</p>
  <p v-if="info" class="banner">
    {{ info }}
    <button v-if="outdated" type="button" @click="reload">Обновить</button>
  </p>

  <section v-if="person" class="card">
    <h2>Редактирование {{ person.id }}</h2>
    <PersonForm :key="formKey" :person="person" @saved="onSaved" />
  </section>
</template>