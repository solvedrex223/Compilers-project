<template>
    <v-expansion-panels>
        <v-expansion-panel expand focusable
            title="Errors"
            :text="text"
            class="error_box"
        ></v-expansion-panel>
    </v-expansion-panels>
</template>

<script setup lang="ts">
    defineProps({
        text: String,
    });
</script>

<style>
    .error_box{
        position: fixed;
    }
</style>