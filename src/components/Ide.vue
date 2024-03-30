<template>
    <v-container id="code_container">
        <v-row id="row_code">
            <v-col>
                <DragList :id="'syntax_list'" :title = "computed_title" :list="list" :group="syn_group"/>
            </v-col>
            <v-col>
                <DragList :id="'code_list'" title="Code" :remove="true" :list="list_code" :group="'name'" @list_update="check_code"/>
            </v-col>
        </v-row>
        <v-row>
            <ErrorBox :text="error_text"></ErrorBox>
        </v-row>
    </v-container>
</template>
<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { ref, watch } from 'vue';
import { useTitleStore } from '../store/stores';

    const syntax_list = [
        { name: "{", statement: undefined },
        { name: "}", statement: undefined },
        { name: ")", statement: undefined },
        { name: ";", statement: undefined },
        { name: "begin", statement: undefined },
        { name: "end", statement: undefined },
        { name: "if (", statement: ''},
        { name: "else {", statement: undefined },
        { name: "while (", statement: '' },
        { name: 'main {', statement: undefined }
    ];
    const var_list = [
        { name: "int", statement: '' },
        { name: "float", statement: '' },
        { name: "String", statement: '' },
    ];
    const class_list = [
        { name: 'new', statement: ''},
        { name: 'class', statement: ''}
    ];
    const default_code = [{name: 'begin', statement: undefined}, {name: 'end', statement: undefined}];
    const syn_group = { name: 'name', pull: 'clone', put: false };
    const {computed_title,title} = storeToRefs(useTitleStore());
    const list = ref([...syntax_list, ...class_list, ...var_list]);
    const list_code = ref(default_code);
    const error_text = ref('');
    class Code {
        code:string = '';
        pos:number = 0;
    }
    
    var code_heap:Array<Code> = [];

    watch(title,(title) => {
        switch (title) {
            case 'all':
                list.value = [...syntax_list, ...class_list, ...var_list];
                break;
            case 'syntax':
                list.value = syntax_list;
                break;
            case 'variables':
                list.value = var_list;
                break;
        }
    });

    function check_code () {
        list_code.value.forEach((code,i) => {
            if (code.statement != undefined || code.name === 'begin') {
                switch (code.name) {
                    case '{':
                    case '}':
                    case ';':
                    case ')':
                        break;
                    default:
                        code_heap.push({code: code.name, pos: i});
                        break;
                }
            }
        });
        var clean_code:Array<number> = [];
        code_heap.forEach((code,pos) => {
            var out = false;
            for (let i = code.pos + 1; i < list_code.value.length; i++) {
                switch (code.code) {
                    case 'begin':
                        if (list_code.value[i].name === 'end') {
                            clean_code.push(pos);
                            out = true;
                        }
                        break;
                    default:
                        if (list_code.value[i].name  === '}') {
                            clean_code.push(pos);
                            out = true;
                        }
                        break;
                }
                if (out) {
                    break;
                }
            }
        });
        clean_code.reverse().forEach(code => {
            code_heap.splice(code,1);
        });
        if(code_heap.length > 0){
            error_text.value = 'Syntax Error. Pos: ' + String(code_heap[code_heap.length - 1].pos);
        }
        else{
            error_text.value = '';
        }
        code_heap = [];
    }
</script>

<style>
    #row_code{
        margin-left: 0%;
        margin-right: 0%;
        height: 50%;
    }
</style>