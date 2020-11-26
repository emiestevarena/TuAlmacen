function search_animal() { 
    let input = document.getElementById('searchbar').value(); 
    input=input.toLowerCase(); 
    let x = document.getElementsByClassName('animals'); 
      
    for (i = 0; i < x.length; i++) {  
        if (!x[i].innerHTML.toLowerCase().includes(input)) { 
            x[i].style.display="none"; 
        } 
        else { 
            x[i].style.display="list-item";                  
        } 
    } 
 }
 
function hideList(input) {
	var datalist = document.getElementbyId("lista");
	if (!datalist[i].innerHTML.toLowerCase().includes(input)) { 
            datalist[i].style.display="none"; 
        } 
        else { 
            datalist[i].style.display="list-item";                  
        } 
}