<script setup>
import { useRouter } from 'vue-router'
import PersonForm from '../components/PersonForm.vue'

const router = useRouter()

function saved(person) {
  router.push(`/person/${person.id}`)
}
</script>

<template>
  <h1>Создать пипл</h1>
  <PersonForm @saved="saved" />
</template>
