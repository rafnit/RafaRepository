$('#mConfirmaExclusaoConta').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	
	var codigoConta = button.data('codigo');
	var descricaoConta = button.data('descricao');
	
	var modal = $(this);
	
	var form = modal.find('form');
	
	var action = form.attr('url-base');
	
	if (!action === "" && !action.endsWith('/')) {
		action += '/';
	}
	
	form.attr('action', action + '/' + codigoConta);
	
	modal.find('.modal-body span').html('Deseja realmente apagar a conta <b>' + descricaoConta + '</b>?');
});


function detailFormatter(index, row) {
    var html = [];
    html.push('<div class="card border-dark mb-3"><div class="card-body">');
    
    $.each(row, function (key, value) {
		if (key.startsWith('col')) {
      		html.push('<p><b>' + key.replace('col', '') + ':</b> ' + value + '</p>');
      	}
    })
    
    html.push('<b>COLOCAR DEPOIS UM CAMPO DE OBSERVACAO QUE PODERA SER EXIBIDO AQUI</div></div>');
    return html.join('')
}


$(function() {
	$('.js-currency').maskMoney({decimal:',',thousands:''});
    
    $('#table').bootstrapTable('destroy').bootstrapTable({
      locale: "pt-BR",
      buttonsOrder: ["fullscreen","columns","toggle"]
    })
});