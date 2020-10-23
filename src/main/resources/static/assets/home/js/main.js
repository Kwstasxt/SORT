$(document).ready(function () {

  $('.table').on('click', 'tr', function () {
    $('.row').removeClass('clicked');
    $('.last-row').removeClass('clicked');
    $(this).toggleClass('clicked');

    var pos = $(this).position();

    $('#executeTradeButton').css({
      top: pos.top + 'px'
    });

    $('#executeTradeButton').attr('value', this.querySelector("#tradeId").value);
    $('#executeTradeButton').removeAttr("disabled");
  });

  $('#price').val(0.01);
  $('#quantity').val(1);

});
