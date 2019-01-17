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
    
 /*   
      let list=$(".dynamic-size");

    for (i = 0; i < list.length; i++) {
        console.log("X");
        let originalSize=$(list[i]).css("max-height");
        let floatOriginalSize=parseFloat(originalSize.substr(0,originalSize.length-2));
        let parent=list[i].parentNode;
        let sizeRemoves=$(parent).find(".size-remove");

        for (j = 0; j < sizeRemoves.length; j++) {
            let removeOriginalSize=$(sizeRemoves[j]).css("height");
            let aRemove=parseFloat(removeOriginalSize.substr(0,removeOriginalSize.length-2));
			floatOriginalSize-=aRemove;
        }


        console.log(floatOriginalSize);
        $(list[i]).css("max-height",floatOriginalSize);
        console.log($(list[i]).css("max-height"));

    }
*/
});