<script setup>
import PersonForm from './PersonForm.vue'

defineProps({ person: { type: Object, required: true } })
defineEmits(['close', 'saved'])
</script>

<template>
  <div class="overlay" @click.self="$emit('close')">
    <div class="dialog">
      <h2>Изменить пипл {{ person.id }}</h2>
      <PersonForm :person="person" @saved="$emit('saved', $event)" />
      <button class="secondary" @click="$emit('close')">Отмена!!</button>
    </div>
  </div>
</template>
