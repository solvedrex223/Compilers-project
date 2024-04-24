<template>
    <v-container id="code_container">
        <v-row id="row_code">
            <v-col>
                <DragList :id="'syntax_list'" :title = "computed_title" v-model="list" :group="syn_group"/>
            </v-col>
            <v-col>
                <DragList :id="'code_list'" title="Code" :remove="true" v-model="list_code" :group="'name'" @list_update="check_code"/>
            </v-col>
        </v-row>
        <v-row>
            <ErrorBox :text="error_text"></ErrorBox>
        </v-row>
        <v-row>
            <div>Compilation Messages</div>
        </v-row>
        <v-row>
            <v-textarea
            :model-value="comp_message"
            readonly
            disabled
            auto-grow
            >
            </v-textarea>
        </v-row>
        <v-row>
            <v-btn
            @click.stop="compile_code"
            >
                Compile
            </v-btn>
        </v-row>
    </v-container>
</template>
<script setup lang="ts">
import { storeToRefs } from 'pinia';
import { ref, watch } from 'vue';
import { useTitleStore } from '../store/stores';

    const syntax_list = [
        { name: "{", statement: undefined, var: undefined },
        { name: "}", statement: undefined, var: undefined },
        { name: ")", statement: undefined, var: undefined },
        { name: ";", statement: undefined },
        { name: "begin", statement: undefined, var: undefined },
        { name: "end", statement: undefined, var: undefined },
        { name: "if (", statement: '', var: undefined},
        { name: "else {", statement: undefined, var: undefined },
        { name: "while (", statement: '', var: undefined },
        { name: 'main {', statement: undefined, var: undefined }
    ];
    const var_list = [
        { name: "int", var: '', statement: '', },
        { name: "float", var: '', statement: '' },
        { name: "String", var: '', statement: '' },
    ];
    const class_list = [
        { name: 'new', statement: '', var: undefined },
        { name: 'class', statement: '', var: undefined }
    ];
    const function_list = [
    { name: 'print(', statement: '', var: undefined }
    ];
    const default_code = [{name: 'begin', statement: undefined, var: undefined}, {name: 'end', statement: undefined, var: undefined}];
    const syn_group = { name: 'name', pull: 'clone', put: false };
    const {computed_title,title} = storeToRefs(useTitleStore());
    const list = ref([...syntax_list, ...class_list, ...var_list, ...function_list]);
    const list_code = ref(default_code);
    const error_text = ref('');
    const comp_message = ref('Test');
    class Code {
        code:string = '';
        pos:number = 0;
    }
    
    var code_heap:Array<Code> = [];

    watch(title,(title) => {
        switch (title) {
            case 'all':
                list.value = [...syntax_list, ...class_list, ...var_list, ...function_list];
                break;
            case 'syntax':
                list.value = syntax_list;
                break;
            case 'variables':
                list.value = var_list;
                break;
            case 'classes':
                list.value = class_list;
                break;
            case 'functions':
                list.value = function_list;
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

    async function compile_code() {
        const list = list_code.value;
        var java_code = '';
        list.forEach(code => {
            if (code.name == 'int' || code.name == 'float' || code.name == 'String'){
                java_code = java_code + code.name + ' ' + code.var + ' = ' + code.statement + ';\n';
            }
            else if (code.name == 'print(') {
                java_code = java_code + 'System.out.println('+ code.statement + ');\n';
            }
            else if(code.name == 'begin'){
                java_code = 'class Code {\n';
            }
            else if(code.name == 'end'){
                java_code = java_code + '}'
            }
            else if(code.name == 'main {'){
                java_code = java_code + 'public static void main(String[] args) {\n';
            }
            else if(code.statement != undefined){
                java_code = java_code + code.name + code.statement + '){\n';
            }
            else{
                java_code = java_code + code.name + '\n';
            }
        });
        const res = await fetch('http://localhost:8080/compile',{
            method: 'POST',
            body: java_code,
            headers: {
                'Content-type': 'text/plain; charset=UTF-8'
            }
        }
        );
        if (res.status == 200) {
            comp_message.value = await res.text();
        }
        else{
            const error = await res.json();
            comp_message.value = error.message;
        }
    }
</script>

<style>
    #row_code{
        margin-left: 0%;
        margin-right: 0%;
        height: 50%;
    }
</style>