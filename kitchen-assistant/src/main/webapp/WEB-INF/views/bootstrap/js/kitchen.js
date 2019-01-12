jQuery(document).ready(function( ) {
    $('input[type=radio]').change(function() {
        if(this.value=="inny") {
                for (i = 0; i < this.parentNode.children.length; i++) {
                    let e=this.parentNode.children[i];
                    if("input"==e.nodeName.toLowerCase()
                        &&"text"==e.type.toLowerCase()
                        &&"inny-url"==e.className){
                        e.disabled=!this.checked;
                    }
                }

            } else {
            let thisRadios=$("[name="+this.name+"]");
            let innyRadio;
            for(j = 0; j < thisRadios.length; j++){
                if(thisRadios[j].value=="inny"){
                    innyRadio=thisRadios[j];
                }
            }
                for (i = 0; i < innyRadio.parentNode.children.length; i++) {
                    let e=innyRadio.parentNode.children[i];
                    if("input"==e.nodeName.toLowerCase()
                        &&"text"==e.type.toLowerCase()
                        &&e.name.endsWith("_innyUrl")){
                        e.disabled=!innyRadio.checked;
                    }
                }
            }

    });

});