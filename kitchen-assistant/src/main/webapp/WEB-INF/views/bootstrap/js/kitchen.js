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


	function updateSubmitButton(){
		if($("[name='liczbaSkladnikow']")[0]!=undefined
				&&Number.parseInt($("[name='liczbaSkladnikow']")[0].value)!=undefined){
			let liczba=Number.parseInt($("[name='liczbaSkladnikow']")[0].value);
			let allChecked=true;
			for(let i=1;i<=liczba;i++){
				if($("[name='skladnik"+i+"_radio']").length>0){
					//there is such radio button checklist
					if($("[name='skladnik"+i+"_radio']:checked").length==0){
						//...but none of the options were checked
						allChecked=false;
					}else{
						//if the checked option was "inny", we should check if text was input
						if($("#skladnik"+i+"_innyUrl")[0]!=undefined
								&&!$("#skladnik"+i+"_innyUrl")[0].disabled
								&&$("#skladnik"+i+"_innyUrl")[0].value==""){
							allChecked=false;
						}
							
					}
	
				}
			}
	
	
			
				let submitButtons=$(".recipe-parsed-submit");
				if(submitButtons.length>0){
					if(allChecked){
						submitButtons[0].disabled=false
					}else{
						submitButtons[0].disabled=true
	
					}
				}
				
				
				
	
		}
	}
	updateSubmitButton();
	let radioList=$("input[type='radio']");
	for(let i=0;i<=radioList!=undefined&&i<radioList.length;i++){
		radioList[i].onchange=function(){
				updateSubmitButton();
		}
	}
	let inneList=$(".inny-url");
	for(let i=0;i<=inneList!=undefined&&i<inneList.length;i++){
		inneList[i].onchange=function(){
				updateSubmitButton();
		}
	}
	
	
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