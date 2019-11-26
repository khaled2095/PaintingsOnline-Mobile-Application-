<?php

// autoload_static.php @generated by Composer

namespace Composer\Autoload;

class ComposerStaticInit610e6c183f5331f8228942ddb6740331
{
    public static $prefixLengthsPsr4 = array (
        'B' => 
        array (
            'Braintree\\' => 10,
        ),
    );

    public static $prefixDirsPsr4 = array (
        'Braintree\\' => 
        array (
            0 => __DIR__ . '/..' . '/braintree/braintree_php/lib/Braintree',
        ),
    );

    public static $prefixesPsr0 = array (
        'B' => 
        array (
            'Braintree' => 
            array (
                0 => __DIR__ . '/..' . '/braintree/braintree_php/lib',
            ),
        ),
    );

    public static function getInitializer(ClassLoader $loader)
    {
        return \Closure::bind(function () use ($loader) {
            $loader->prefixLengthsPsr4 = ComposerStaticInit610e6c183f5331f8228942ddb6740331::$prefixLengthsPsr4;
            $loader->prefixDirsPsr4 = ComposerStaticInit610e6c183f5331f8228942ddb6740331::$prefixDirsPsr4;
            $loader->prefixesPsr0 = ComposerStaticInit610e6c183f5331f8228942ddb6740331::$prefixesPsr0;

        }, null, ClassLoader::class);
    }
}
