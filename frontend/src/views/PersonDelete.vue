<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { personApi } from '../api'
import { onPersonChanged } from '../live'

const idInput = ref('')
const person = ref(null)
const error = ref('')
const success = ref('')
const info = ref('')
const deleting = ref(false)

function clearMessages() {
  error.value = ''
  success.value = ''
  info.value = ''
}

function gone(id) {
  person.value = null
  clearMessages()
  info.value = `Пипл ${id} уже удалён в другом окне`
}

async function findPerson() {
  clearMessages()
  person.value = null

  const id = Number(idInput.value)
  if (!Number.isInteger(id)) {
    error.value = 'ID должен быть целым числом'
    return
  }

  try {
    person.value = await personApi.get(id)
  } catch (e) {
    error.value = e.message
  }
}

async function removePerson() {
  if (!person.value || deleting.value) return

  const { id, name } = person.value
  deleting.value = true
  clearMessages()

  try {
    await personApi.remove(id)
    person.value = null
    success.value = `Пипл ${id} «${name}» удалён`
  } catch (e) {
    if (e.status === 404) gone(id)
    else error.value = e.message
  } finally {
    deleting.value = false
  }
}

async function refresh(id) {
  try {
    person.value = await personApi.get(id)
    error.value = ''
    info.value = `Данные пипла ${id} изменены в другом окне, показана актуальная версия`
  } catch (e) {
    if (e.status === 404) gone(id)
    else error.value = e.message
  }
}

let off
onMounted(() => {
  off = onPersonChanged((message) => {
    if (deleting.value) return
    if (!person.value || String(message.id) !== String(person.value.id)) return

    if (message.type === 'deleted') gone(message.id)
    else if (message.type === 'updated') refresh(message.id)
  })
})
onUnmounted(() => off?.())
</script>

<template>
  <h1>Удалить пипл</h1>

  <form class="inline" @submit.prevent="findPerson">
    <input
        v-model="idInput"
        type="number"
        min="1"
        step="1"
        placeholder="ID человека"
        required
    />
    <button type="submit">Найти</button>
  </form>

  <p v-if="error" class="banner error">{{ error }}</p>
  <p v-if="success" class="banner">{{ success }}</p>
  <p v-if="info" class="banner">{{ info }}</p>

  <section v-if="person" class="card">
    <h2>Удалить {{ person.id }}</h2>

    <p>Имя: {{ person.name }}</p>
    <p>Рост: {{ person.height }}</p>

    <button class="danger" :disabled="deleting" @click="removePerson">
      Удалить
    </button>
  </section>
</template>