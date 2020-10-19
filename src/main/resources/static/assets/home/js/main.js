$(document).ready(function () {

  $('.table').on('click', 'tr', function () {
    $('.row').removeClass('clicked');
    $('.last-row').removeClass('clicked');
    $(this).toggleClass('clicked');

    var pos = $(this).position();

    $('#executeTradeButton').css({
      top: pos.top + 'px'
    });
  });

});
