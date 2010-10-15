jQuery.noConflict();

function dressEditables() {
	jQuery(".edit").editable(
			function(value, settings) { 
				var valueToSend = value;
				if(value.match(/^'+$/)) {
					valueToSend+="''";
				}
				if (value.match(/^"+$/)) {
					valueToSend+="\"\"";
				}
				XT.doAjaxAction('saveField', this, {'text' : valueToSend});
			    return value;
		 	},
		 	{
		 		tooltip	: 'Click here to edit',
		 		cancel	: 'Cancel',
		 		submit	: 'OK',
		 		placeholder : 'Click here',
		 		onblur : 'ignore',
		 		callback : function(innerHtml, settings) {
		 			jQuery(this).html(innerHtml + '<span class="loadingIndicator"><img src="../img/ajax-loader.gif"/><b>Saving</b></span>');
		 		}
		 	}
		);
}

jQuery(document).ready(function() {

	dressEditables();
 	dressRubricLinks();
 	
 	jQuery(".articlePic").picChanger('setPic');
 	
 	jQuery('.bannerBox').bannerEditor();
 	
 	jQuery('.imageBox').imageEditor();
	
	jQuery('.slideAdder').slideAdder('newSlide'); 	
	
 	jQuery.extend(DateInput.DEFAULT_OPTS, {
	  month_names: ["Sausis", "Vasaris", "Kovas", "Balandis", "Gegužė", "Birželis", "Liepa", "Rugpjūtis", "Rugsėjis", "Spalis", "Lapkritis", "Gruodis"],
	  short_month_names: ["Sausis", "Vasaris", "Kovas", "Balandis", "Gegužė", "Birželis", "Liepa", "Rugpjūtis", "Rugsėjis", "Spalis", "Lapkritis", "Gruodis"],
	  short_day_names: ["Sekm", "Pirm", "Antr", "Treč", "Ketv", "Penk", "Šešt"],
	  
	  stringToDate: function(string) {
	    var matches;
	    if (matches = string.match(/^(\d{4,4})-(\d{2,2})-(\d{2,2})$/)) {
	      return new Date(matches[1], matches[2] - 1, matches[3]);
	    } else {
	      return null;
	    };
	  },
	
	  dateToString: function(date) {
	    var month = (date.getMonth() + 1).toString();
	    var dom = date.getDate().toString();
	    if (month.length == 1) month = "0" + month;
	    if (dom.length == 1) dom = "0" + dom;
	    return date.getFullYear() + "-" + month + "-" + dom;
	  }
	  
	});

 	jQuery(".date_input").date_input();
 	jQuery("#articleControlsHeader").click(function(){jQuery("#articleControls").toggle()});
 	//initHelp();
 	
});

function initHelp() {
	jQuery(".edit").bt("Spragtelėkite pele, norėdami redaguoti");
	jQuery(".help-staticAddress").bt("Čia galite nurodyti šio puslapio statinį adresą, t.y. fiksuotą gražų adresą, kuris nesikeis veikiant sistemai ir kuris patinga Googlui.");
	jQuery(".help-makeRubricOnline").bt("Paspaudus šį paukščiuką puslapis taps prieinamas lankytojams");
	jQuery(".help-deleteRubric").bt("Paspaudus šį mygtuką puslapis bus ištrintas. Ištrintus puslapius galite atkurti paspausdami mygtuką Undo");
	jQuery(".help-makeRubricOffline").bt("Paspaudus šį mygtuką puslapis bus paslėptas nuo lankytojų");
	jQuery(".help-leaf").bt("Puslapiai pažymėti šiuo paukščiuku bus nerodomi meniu");
	jQuery(".help-visibleForever").bt("Šiuo paukščiuku nurodoma, ar puslapis matomas visą laiką ar turi rodymo pradžios ir pabaigos datas");
	jQuery(".help-addSubrubric").bt("Spustelėkite čia norėdami pridėti papildomą svetainės rubriką. Nauja rubrika bus pridėta kaip einamosios rubrikos parubrikė");
//	jQuery(".").bt("");
//	jQuery(".").bt("");
//	jQuery(".").bt("");
	
	
}