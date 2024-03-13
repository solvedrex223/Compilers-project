<template>
    <h1 class="code_list_title">{{ title }}</h1>
    <draggable
    :list="list"
    class="code_list overflow-y-auto"
    :id="id"
    :group="group"
    item-key="name">
        <template #item="{element}">
            <div class="code_list_item">{{element.name}}</div>
        </template>
    </draggable>
</template>

<script setup lang="ts">
import { defineComponent } from 'vue';
import Draggable from 'vuedraggable';

    defineComponent({
        Draggable
    });

    defineProps({
        id: String,
        title: {
            default: 'All',
            type: String,
        },
        list: {
            default(){return [
                { name: "begin" },
                { name: "end" }
            ]},
            type: Array
        },
        group: {}
    });
</script>

<style>
    .code_list{
        position: relative;
        left:3%;
        padding: .5%;
        border-style: solid;
        border-color: white;
        border-width: 1px;
        max-width: 60%;
        margin-left: auto;
        margin-right: auto;
    }
    .code_list_item{
        position: inherit;
        text-align: center;
        border-width: 1px;
        border-color: white;
        border-style: solid;
        font-size: 30px;
    }
    .code_list_title{
        position: relative;
        left:3%;
        text-align: center;
        padding-top: 1%;
    }
</style>