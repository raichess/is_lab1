<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { personApi } from '../api'
import PersonForm from '../components/PersonForm.vue'

const router = useRouter()

const idInput = ref('')
const person = ref(null)
const error = ref('')

async function findPerson() {
  const id = Number(idInput.value)

  if (!Number.isInteger(id) || id <= 0) {
    error.value = 'ID должен быть целым числом и больше 0'
    person.value = null
    return
  }

  error.value = ''
  person.value = null

  try {
    person.value = await personApi.get(id)
  } catch (e) {
    error.value = e.message
  }
}

function onSaved(savedPerson) {
  router.push(`/person/${savedPerson.id}`)
}
</script>

<template>
  <main class="action-page">
    <h1>Апдейт </h1>

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

    <section v-if="person" class="card">
      <h2>Редактирование {{ person.id }}</h2>
      <PersonForm
          :person="person"
          @saved="onSaved"
      />
    </section>
  </main>
</template>
