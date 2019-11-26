<?php
/*
Description: API Braintree Payment.
Author: Ziad Tamim
Version: 1.0
Author URI: https://intensifystudio.com
*/

require 'vendor/autoload.php';
require 'Check.php';

Braintree_Configuration::environment('sandbox');
Braintree_Configuration::merchantId('m5jfgdsggqkp5yyw');
Braintree_Configuration::publicKey('7dxr3fvsxzgz6j8d');
Braintree_Configuration::privateKey('98b70e20e449c61a4124606125c88c45');

// Get the credit card details submitted by the form
$paymentMethodNonce =  $_POST['payment_method_nonce'];
$amount = $_POST['amount'];

$result = Braintree_Transaction::sale([
  'amount' => $amount,
  'paymentMethodNonce' => $paymentMethodNonce,
  'options' => [
    'submitForSettlement' => False
  ]
]);
$result = CheckForResult($result);
echo json_encode($result);
?>
