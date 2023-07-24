/**
 * Namespace for hashing and other cryptographic functions
 * Copyright (c) Andrew Valums
 * Licensed under the MIT license, http://valums.com/mit-license/
 */

var V = V || {};
V.Security = V.Security || {};

( function() {
  // for faster access
  var S = V.Security;

  /**
   * The highest integer value a number can go to without losing precision.
   */
  S.maxExactInt = Math.pow( 2, 53 );

  /**
   * Converts string from internal UTF-16 to UTF-8
   * and saves it using array of numbers (bytes), 0-255 per cell
   * @param {String} str
   * @return {Array}
   */
  S.toUtf8ByteArr = function( str ) {
    var arr = [],
        code;

    for( var i = 0;i < str.length;i++ ) {
      code = str.charCodeAt( i );

      /*
       Note that charCodeAt will always return a value that is less than 65,536.
       This is because the higher code points are represented by a pair of (lower valued)
       "surrogate" pseudo-characters which are used to comprise the real character.
       Because of this, in order to examine or reproduce the full character for
       individual characters of value 65,536 and above, for such characters,
       it is necessary to retrieve not only charCodeAt(0), but also charCodeAt(1).
       */
      if( 0xD800 <= code && code <= 0xDBFF ) {
        // UTF-16 high surrogate
        var hi = code,
            low = str.charCodeAt( i + 1 );

        code = ( ( hi - 0xD800 ) * 0x400 ) + ( low - 0xDC00 ) + 0x10000;

        i++;
      }

      if( code <= 127 ) {
        arr[ arr.length ] = code;
      } else if( code <= 2047 ) {
        arr[ arr.length ] = ( code >>> 6 ) + 0xC0;
        arr[ arr.length ] = code & 0x3F | 0x80;
      } else if( code <= 65535 ) {
        arr[ arr.length ] = ( code >>> 12 ) + 0xE0;
        arr[ arr.length ] = ( code >>> 6 & 0x3F ) | 0x80;
        arr[ arr.length ] = ( code & 0x3F ) | 0x80;
      } else if( code <= 1114111 ) {
        arr[ arr.length ] = ( code >>> 18 ) + 0xF0;
        arr[ arr.length ] = ( code >>> 12 & 0x3F ) | 0x80;
        arr[ arr.length ] = ( code >>> 6 & 0x3F ) | 0x80;
        arr[ arr.length ] = ( code & 0x3F ) | 0x80;
      } else {
        throw 'Unicode standart supports code points up-to U+10FFFF';
      }
    }

    return arr;
  };

  /**
   * Outputs 32 integer bits of a number in hex format.
   * Preserves leading zeros.
   * @param {Number} num
   */
  S.toHex32 = function( num ) {
    // if negative
    if( num & 0x80000000 ) {
      // convert to positive number
      num = num & ( ~0x80000000 );
      num += Math.pow( 2, 31 );
    }

    var str = num.toString( 16 );

    while( str.length < 8 ) {
      str = '0' + str;
    }

    return str;
  };

  /**
   * Changes the order of 4 bytes in integer representation of number.
   * From 1234 to 4321.
   * @param {Number} num Only 32 int bits are used.
   */
  S.reverseBytes = function( num ) {
    var res = 0;
    res += ( ( num >>> 24 ) & 0xff );
    res += ( ( num >>> 16 ) & 0xff ) << 8;
    res += ( ( num >>> 8 ) & 0xff ) << 16;
    res += ( num & 0xff ) << 24;
    return res;
  };

  S.leftRotate = function( x, c ) {
    return ( x << c ) | ( x >>> ( 32 - c ) );
  };

  /**
   * RSA Data Security, Inc. MD5 Message-Digest Algorithm
   * http://tools.ietf.org/html/rfc1321
   * http://en.wikipedia.org/wiki/MD5
   * @param {String} message
   */
  S.md5 = function( message ) {
    // r specifies the per-round shift amounts
    var r = [ 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21 ];

    // Use binary integer part of the sines of integers (Radians) as constants:
    var k = [];
    for( var i = 0;i <= 63;i++ ) {
      k[ i ] = ( Math.abs( Math.sin( i + 1 ) ) * Math.pow( 2, 32 ) ) << 0;
    }

    var h0 = 0x67452301,
        h1 = 0xEFCDAB89,
        h2 = 0x98BADCFE,
        h3 = 0x10325476,
        bytes, unpadded;

    //Pre-processing:
    bytes = S.toUtf8ByteArr( message );
    message = null;
    unpadded = bytes.length;

    //append "1" bit to message
    //append "0" bits until message length in bits ≡ 448 (mod 512)
    bytes.push( 0x80 );
    var zeroBytes = Math.abs( 448 - ( bytes.length * 8 ) % 512 ) / 8;

    while( zeroBytes-- ) {
      bytes.push( 0 );
    }

    //append bit length of unpadded message as 64-bit little-endian integer to message
    bytes.push( unpadded * 8 & 0xff, unpadded * 8 >> 8 & 0xff, unpadded * 8 >> 16 & 0xff, unpadded * 8 >> 24 & 0xff );

    var i = 4;
    while( i-- ) {
      bytes.push( 0 );
    }

    var leftRotate = S.leftRotate;

    //Process the message in successive 512-bit chunks:
    var i = 0,
        w = [];
    while( i < bytes.length ) {

      //break chunk into sixteen 32-bit words w[i], 0 ≤ i ≤ 15
      for( var j = 0;j <= 15;j++ ) {
        w[ j ] = ( bytes[ i + 4 * j ] << 0 ) + ( bytes[ i + 4 * j + 1 ] << 8 ) + ( bytes[ i + 4 * j + 2 ] << 16 ) + ( bytes[ i + 4 * j + 3 ] << 24 );
      }

      //Initialize hash value for this chunk:
      var a = h0,
          b = h1,
          c = h2,
          d = h3,
          f, g;

      //Main loop:
      for( var j = 0;j <= 63;j++ ) {

        if( j <= 15 ) {
          f = ( b & c ) | ( ( ~b ) & d );
          g = j;
        } else if( j <= 31 ) {
          f = ( d & b ) | ( ( ~d ) & c );
          g = ( 5 * j + 1 ) % 16;
        } else if( j <= 47 ) {
          f = b ^ c ^ d;
          g = ( 3 * j + 5 ) % 16;
        } else {
          f = c ^ ( b | ( ~d ) );
          g = ( 7 * j ) % 16;
        }

        var temp = d;

        d = c;
        c = b;
        b = b + leftRotate(( a + f + k[ j ] + w[ g ] ), r[ j ] );
        a = temp;
      }

      //Add this chunk's hash to result so far:
      h0 = ( h0 + a ) << 0;
      h1 = ( h1 + b ) << 0;
      h2 = ( h2 + c ) << 0;
      h3 = ( h3 + d ) << 0;

      i += 512 / 8;
    }

    // fix when starting with 0
    var res = out( h0 ) + out( h1 ) + out( h2 ) + out( h3 );

    function out( h ) {
      return S.toHex32( S.reverseBytes( h ) );
    }

    return res;
  };
})();


/*
 * A JavaScript implementation of the RSA Data Security, Inc. MD5 Message
 * Digest Algorithm, as defined in RFC 1321.
 * Version 2.2 Copyright (C) Paul Johnston 1999 - 2009
 * Other contributors: Greg Holt, Andrew Kepert, Ydnar, Lostinet
 * Distributed under the BSD License
 * See http://pajhome.org.uk/crypt/md5 for more info.
 */

/*
 * Configurable variables. You may need to tweak these to be compatible with
 * the server-side, but the defaults work in most cases.
 */
var hexcase = 0; /* hex output format. 0 - lowercase; 1 - uppercase        */
var b64pad = ""; /* base-64 pad character. "=" for strict RFC compliance   */

/*
 * These are the functions you'll usually want to call
 * They take string arguments and return either hex or base-64 encoded strings
 */

function hex_md5( s ) {
  return rstr2hex( rstr_md5( str2rstr_utf8( s ) ) );
}

function b64_md5( s ) {
  return rstr2b64( rstr_md5( str2rstr_utf8( s ) ) );
}

function any_md5( s, e ) {
  return rstr2any( rstr_md5( str2rstr_utf8( s ) ), e );
}

function hex_hmac_md5( k, d ) {
  return rstr2hex( rstr_hmac_md5( str2rstr_utf8( k ), str2rstr_utf8( d ) ) );
}

function b64_hmac_md5( k, d ) {
  return rstr2b64( rstr_hmac_md5( str2rstr_utf8( k ), str2rstr_utf8( d ) ) );
}

function any_hmac_md5( k, d, e ) {
  return rstr2any( rstr_hmac_md5( str2rstr_utf8( k ), str2rstr_utf8( d ) ), e );
}

/*
 * Perform a simple self-test to see if the VM is working
 */

function md5_vm_test() {
  return hex_md5( "abc" ).toLowerCase() == "900150983cd24fb0d6963f7d28e17f72";
}

/*
 * Calculate the MD5 of a raw string
 */

function rstr_md5( s ) {
  return binl2rstr( binl_md5( rstr2binl( s ), s.length * 8 ) );
}

/*
 * Calculate the HMAC-MD5, of a key and some data (raw strings)
 */

function rstr_hmac_md5( key, data ) {
  var bkey = rstr2binl( key );
  if( bkey.length > 16 ) bkey = binl_md5( bkey, key.length * 8 );

  var ipad = Array( 16 ),
      opad = Array( 16 );
  for( var i = 0;i < 16;i++ ) {
    ipad[ i ] = bkey[ i ] ^ 0x36363636;
    opad[ i ] = bkey[ i ] ^ 0x5C5C5C5C;
  }

  var hash = binl_md5( ipad.concat( rstr2binl( data ) ), 512 + data.length * 8 );
  return binl2rstr( binl_md5( opad.concat( hash ), 512 + 128 ) );
}

/*
 * Convert a raw string to a hex string
 */

function rstr2hex( input ) {
  try {
    hexcase
  } catch( e ) {
    hexcase = 0;
  }
  var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
  var output = "";
  var x;
  for( var i = 0;i < input.length;i++ ) {
    x = input.charCodeAt( i );
    output += hex_tab.charAt(( x >>> 4 ) & 0x0F ) + hex_tab.charAt( x & 0x0F );
  }
  return output;
}

/*
 * Convert a raw string to a base-64 string
 */

function rstr2b64( input ) {
  try {
    b64pad
  } catch( e ) {
    b64pad = '';
  }
  var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  var output = "";
  var len = input.length;
  for( var i = 0;i < len;i += 3 ) {
    var triplet = ( input.charCodeAt( i ) << 16 ) | ( i + 1 < len ? input.charCodeAt( i + 1 ) << 8 : 0 ) | ( i + 2 < len ? input.charCodeAt( i + 2 ) : 0 );
    for( var j = 0;j < 4;j++ ) {
      if( i * 8 + j * 6 > input.length * 8 ) output += b64pad;
      else output += tab.charAt(( triplet >>> 6 * ( 3 - j ) ) & 0x3F );
    }
  }
  return output;
}

/*
 * Convert a raw string to an arbitrary string encoding
 */

function rstr2any( input, encoding ) {
  var divisor = encoding.length;
  var i, j, q, x, quotient;

  /* Convert to an array of 16-bit big-endian values, forming the dividend */
  var dividend = Array( Math.ceil( input.length / 2 ) );
  for( i = 0;i < dividend.length;i++ ) {
    dividend[ i ] = ( input.charCodeAt( i * 2 ) << 8 ) | input.charCodeAt( i * 2 + 1 );
  }

  /*
   * Repeatedly perform a long division. The binary array forms the dividend,
   * the length of the encoding is the divisor. Once computed, the quotient
   * forms the dividend for the next step. All remainders are stored for later
   * use.
   */
  var full_length = Math.ceil( input.length * 8 / ( Math.log( encoding.length ) / Math.log( 2 ) ) );
  var remainders = Array( full_length );
  for( j = 0;j < full_length;j++ ) {
    quotient = Array();
    x = 0;
    for( i = 0;i < dividend.length;i++ ) {
      x = ( x << 16 ) + dividend[ i ];
      q = Math.floor( x / divisor );
      x -= q * divisor;
      if( quotient.length > 0 || q > 0 ) quotient[ quotient.length ] = q;
    }
    remainders[ j ] = x;
    dividend = quotient;
  }

  /* Convert the remainders to the output string */
  var output = "";
  for( i = remainders.length - 1;i >= 0;i-- )
    output += encoding.charAt( remainders[ i ] );

  return output;
}

/*
 * Encode a string as utf-8.
 * For efficiency, this assumes the input is valid utf-16.
 */

function str2rstr_utf8( input ) {
  return unescape( encodeURI( input ) );
}

/*
 * Encode a string as utf-16
 */

function str2rstr_utf16le( input ) {
  var output = "";
  for( var i = 0;i < input.length;i++ )
    output += String.fromCharCode( input.charCodeAt( i ) & 0xFF, ( input.charCodeAt( i ) >>> 8 ) & 0xFF );
  return output;
}

function str2rstr_utf16be( input ) {
  var output = "";
  for( var i = 0;i < input.length;i++ )
    output += String.fromCharCode(( input.charCodeAt( i ) >>> 8 ) & 0xFF, input.charCodeAt( i ) & 0xFF );
  return output;
}

/*
 * Convert a raw string to an array of little-endian words
 * Characters >255 have their high-byte silently ignored.
 */

function rstr2binl( input ) {
  var output = Array( input.length >> 2 );
  for( var i = 0;i < output.length;i++ )
    output[ i ] = 0;
  for( var i = 0;i < input.length * 8;i += 8 )
    output[ i >> 5 ] |= ( input.charCodeAt( i / 8 ) & 0xFF ) << ( i % 32 );
  return output;
}

/*
 * Convert an array of little-endian words to a string
 */

function binl2rstr( input ) {
  var output = "";
  for( var i = 0;i < input.length * 32;i += 8 )
    output += String.fromCharCode(( input[ i >> 5 ] >>> ( i % 32 ) ) & 0xFF );
  return output;
}

/*
 * Calculate the MD5 of an array of little-endian words, and a bit length.
 */

function binl_md5( x, len ) { /* append padding */
  x[ len >> 5 ] |= 0x80 << ( ( len ) % 32 );
  x[ ( ( ( len + 64 ) >>> 9 ) << 4 ) + 14 ] = len;

  var a = 1732584193;
  var b = -271733879;
  var c = -1732584194;
  var d = 271733878;

  for( var i = 0;i < x.length;i += 16 ) {
    var olda = a;
    var oldb = b;
    var oldc = c;
    var oldd = d;

    a = md5_ff( a, b, c, d, x[ i + 0 ], 7, -680876936 );
    d = md5_ff( d, a, b, c, x[ i + 1 ], 12, -389564586 );
    c = md5_ff( c, d, a, b, x[ i + 2 ], 17, 606105819 );
    b = md5_ff( b, c, d, a, x[ i + 3 ], 22, -1044525330 );
    a = md5_ff( a, b, c, d, x[ i + 4 ], 7, -176418897 );
    d = md5_ff( d, a, b, c, x[ i + 5 ], 12, 1200080426 );
    c = md5_ff( c, d, a, b, x[ i + 6 ], 17, -1473231341 );
    b = md5_ff( b, c, d, a, x[ i + 7 ], 22, -45705983 );
    a = md5_ff( a, b, c, d, x[ i + 8 ], 7, 1770035416 );
    d = md5_ff( d, a, b, c, x[ i + 9 ], 12, -1958414417 );
    c = md5_ff( c, d, a, b, x[ i + 10 ], 17, -42063 );
    b = md5_ff( b, c, d, a, x[ i + 11 ], 22, -1990404162 );
    a = md5_ff( a, b, c, d, x[ i + 12 ], 7, 1804603682 );
    d = md5_ff( d, a, b, c, x[ i + 13 ], 12, -40341101 );
    c = md5_ff( c, d, a, b, x[ i + 14 ], 17, -1502002290 );
    b = md5_ff( b, c, d, a, x[ i + 15 ], 22, 1236535329 );

    a = md5_gg( a, b, c, d, x[ i + 1 ], 5, -165796510 );
    d = md5_gg( d, a, b, c, x[ i + 6 ], 9, -1069501632 );
    c = md5_gg( c, d, a, b, x[ i + 11 ], 14, 643717713 );
    b = md5_gg( b, c, d, a, x[ i + 0 ], 20, -373897302 );
    a = md5_gg( a, b, c, d, x[ i + 5 ], 5, -701558691 );
    d = md5_gg( d, a, b, c, x[ i + 10 ], 9, 38016083 );
    c = md5_gg( c, d, a, b, x[ i + 15 ], 14, -660478335 );
    b = md5_gg( b, c, d, a, x[ i + 4 ], 20, -405537848 );
    a = md5_gg( a, b, c, d, x[ i + 9 ], 5, 568446438 );
    d = md5_gg( d, a, b, c, x[ i + 14 ], 9, -1019803690 );
    c = md5_gg( c, d, a, b, x[ i + 3 ], 14, -187363961 );
    b = md5_gg( b, c, d, a, x[ i + 8 ], 20, 1163531501 );
    a = md5_gg( a, b, c, d, x[ i + 13 ], 5, -1444681467 );
    d = md5_gg( d, a, b, c, x[ i + 2 ], 9, -51403784 );
    c = md5_gg( c, d, a, b, x[ i + 7 ], 14, 1735328473 );
    b = md5_gg( b, c, d, a, x[ i + 12 ], 20, -1926607734 );

    a = md5_hh( a, b, c, d, x[ i + 5 ], 4, -378558 );
    d = md5_hh( d, a, b, c, x[ i + 8 ], 11, -2022574463 );
    c = md5_hh( c, d, a, b, x[ i + 11 ], 16, 1839030562 );
    b = md5_hh( b, c, d, a, x[ i + 14 ], 23, -35309556 );
    a = md5_hh( a, b, c, d, x[ i + 1 ], 4, -1530992060 );
    d = md5_hh( d, a, b, c, x[ i + 4 ], 11, 1272893353 );
    c = md5_hh( c, d, a, b, x[ i + 7 ], 16, -155497632 );
    b = md5_hh( b, c, d, a, x[ i + 10 ], 23, -1094730640 );
    a = md5_hh( a, b, c, d, x[ i + 13 ], 4, 681279174 );
    d = md5_hh( d, a, b, c, x[ i + 0 ], 11, -358537222 );
    c = md5_hh( c, d, a, b, x[ i + 3 ], 16, -722521979 );
    b = md5_hh( b, c, d, a, x[ i + 6 ], 23, 76029189 );
    a = md5_hh( a, b, c, d, x[ i + 9 ], 4, -640364487 );
    d = md5_hh( d, a, b, c, x[ i + 12 ], 11, -421815835 );
    c = md5_hh( c, d, a, b, x[ i + 15 ], 16, 530742520 );
    b = md5_hh( b, c, d, a, x[ i + 2 ], 23, -995338651 );

    a = md5_ii( a, b, c, d, x[ i + 0 ], 6, -198630844 );
    d = md5_ii( d, a, b, c, x[ i + 7 ], 10, 1126891415 );
    c = md5_ii( c, d, a, b, x[ i + 14 ], 15, -1416354905 );
    b = md5_ii( b, c, d, a, x[ i + 5 ], 21, -57434055 );
    a = md5_ii( a, b, c, d, x[ i + 12 ], 6, 1700485571 );
    d = md5_ii( d, a, b, c, x[ i + 3 ], 10, -1894986606 );
    c = md5_ii( c, d, a, b, x[ i + 10 ], 15, -1051523 );
    b = md5_ii( b, c, d, a, x[ i + 1 ], 21, -2054922799 );
    a = md5_ii( a, b, c, d, x[ i + 8 ], 6, 1873313359 );
    d = md5_ii( d, a, b, c, x[ i + 15 ], 10, -30611744 );
    c = md5_ii( c, d, a, b, x[ i + 6 ], 15, -1560198380 );
    b = md5_ii( b, c, d, a, x[ i + 13 ], 21, 1309151649 );
    a = md5_ii( a, b, c, d, x[ i + 4 ], 6, -145523070 );
    d = md5_ii( d, a, b, c, x[ i + 11 ], 10, -1120210379 );
    c = md5_ii( c, d, a, b, x[ i + 2 ], 15, 718787259 );
    b = md5_ii( b, c, d, a, x[ i + 9 ], 21, -343485551 );

    a = safe_add( a, olda );
    b = safe_add( b, oldb );
    c = safe_add( c, oldc );
    d = safe_add( d, oldd );
  }
  return Array( a, b, c, d );
}

/*
 * These functions implement the four basic operations the algorithm uses.
 */

function md5_cmn( q, a, b, x, s, t ) {
  return safe_add( bit_rol( safe_add( safe_add( a, q ), safe_add( x, t ) ), s ), b );
}

function md5_ff( a, b, c, d, x, s, t ) {
  return md5_cmn(( b & c ) | ( ( ~b ) & d ), a, b, x, s, t );
}

function md5_gg( a, b, c, d, x, s, t ) {
  return md5_cmn(( b & d ) | ( c & ( ~d ) ), a, b, x, s, t );
}

function md5_hh( a, b, c, d, x, s, t ) {
  return md5_cmn( b ^ c ^ d, a, b, x, s, t );
}

function md5_ii( a, b, c, d, x, s, t ) {
  return md5_cmn( c ^ ( b | ( ~d ) ), a, b, x, s, t );
}

/*
 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
 * to work around bugs in some JS interpreters.
 */

function safe_add( x, y ) {
  var lsw = ( x & 0xFFFF ) + ( y & 0xFFFF );
  var msw = ( x >> 16 ) + ( y >> 16 ) + ( lsw >> 16 );
  return ( msw << 16 ) | ( lsw & 0xFFFF );
}

/*
 * Bitwise rotate a 32-bit number to the left.
 */

function bit_rol( num, cnt ) {
  return ( num << cnt ) | ( num >>> ( 32 - cnt ) );
}


function md5cycle( x, k ) {
  var a = x[ 0 ],
      b = x[ 1 ],
      c = x[ 2 ],
      d = x[ 3 ];

  a = ff( a, b, c, d, k[ 0 ], 7, -680876936 );
  d = ff( d, a, b, c, k[ 1 ], 12, -389564586 );
  c = ff( c, d, a, b, k[ 2 ], 17, 606105819 );
  b = ff( b, c, d, a, k[ 3 ], 22, -1044525330 );
  a = ff( a, b, c, d, k[ 4 ], 7, -176418897 );
  d = ff( d, a, b, c, k[ 5 ], 12, 1200080426 );
  c = ff( c, d, a, b, k[ 6 ], 17, -1473231341 );
  b = ff( b, c, d, a, k[ 7 ], 22, -45705983 );
  a = ff( a, b, c, d, k[ 8 ], 7, 1770035416 );
  d = ff( d, a, b, c, k[ 9 ], 12, -1958414417 );
  c = ff( c, d, a, b, k[ 10 ], 17, -42063 );
  b = ff( b, c, d, a, k[ 11 ], 22, -1990404162 );
  a = ff( a, b, c, d, k[ 12 ], 7, 1804603682 );
  d = ff( d, a, b, c, k[ 13 ], 12, -40341101 );
  c = ff( c, d, a, b, k[ 14 ], 17, -1502002290 );
  b = ff( b, c, d, a, k[ 15 ], 22, 1236535329 );

  a = gg( a, b, c, d, k[ 1 ], 5, -165796510 );
  d = gg( d, a, b, c, k[ 6 ], 9, -1069501632 );
  c = gg( c, d, a, b, k[ 11 ], 14, 643717713 );
  b = gg( b, c, d, a, k[ 0 ], 20, -373897302 );
  a = gg( a, b, c, d, k[ 5 ], 5, -701558691 );
  d = gg( d, a, b, c, k[ 10 ], 9, 38016083 );
  c = gg( c, d, a, b, k[ 15 ], 14, -660478335 );
  b = gg( b, c, d, a, k[ 4 ], 20, -405537848 );
  a = gg( a, b, c, d, k[ 9 ], 5, 568446438 );
  d = gg( d, a, b, c, k[ 14 ], 9, -1019803690 );
  c = gg( c, d, a, b, k[ 3 ], 14, -187363961 );
  b = gg( b, c, d, a, k[ 8 ], 20, 1163531501 );
  a = gg( a, b, c, d, k[ 13 ], 5, -1444681467 );
  d = gg( d, a, b, c, k[ 2 ], 9, -51403784 );
  c = gg( c, d, a, b, k[ 7 ], 14, 1735328473 );
  b = gg( b, c, d, a, k[ 12 ], 20, -1926607734 );

  a = hh( a, b, c, d, k[ 5 ], 4, -378558 );
  d = hh( d, a, b, c, k[ 8 ], 11, -2022574463 );
  c = hh( c, d, a, b, k[ 11 ], 16, 1839030562 );
  b = hh( b, c, d, a, k[ 14 ], 23, -35309556 );
  a = hh( a, b, c, d, k[ 1 ], 4, -1530992060 );
  d = hh( d, a, b, c, k[ 4 ], 11, 1272893353 );
  c = hh( c, d, a, b, k[ 7 ], 16, -155497632 );
  b = hh( b, c, d, a, k[ 10 ], 23, -1094730640 );
  a = hh( a, b, c, d, k[ 13 ], 4, 681279174 );
  d = hh( d, a, b, c, k[ 0 ], 11, -358537222 );
  c = hh( c, d, a, b, k[ 3 ], 16, -722521979 );
  b = hh( b, c, d, a, k[ 6 ], 23, 76029189 );
  a = hh( a, b, c, d, k[ 9 ], 4, -640364487 );
  d = hh( d, a, b, c, k[ 12 ], 11, -421815835 );
  c = hh( c, d, a, b, k[ 15 ], 16, 530742520 );
  b = hh( b, c, d, a, k[ 2 ], 23, -995338651 );

  a = ii( a, b, c, d, k[ 0 ], 6, -198630844 );
  d = ii( d, a, b, c, k[ 7 ], 10, 1126891415 );
  c = ii( c, d, a, b, k[ 14 ], 15, -1416354905 );
  b = ii( b, c, d, a, k[ 5 ], 21, -57434055 );
  a = ii( a, b, c, d, k[ 12 ], 6, 1700485571 );
  d = ii( d, a, b, c, k[ 3 ], 10, -1894986606 );
  c = ii( c, d, a, b, k[ 10 ], 15, -1051523 );
  b = ii( b, c, d, a, k[ 1 ], 21, -2054922799 );
  a = ii( a, b, c, d, k[ 8 ], 6, 1873313359 );
  d = ii( d, a, b, c, k[ 15 ], 10, -30611744 );
  c = ii( c, d, a, b, k[ 6 ], 15, -1560198380 );
  b = ii( b, c, d, a, k[ 13 ], 21, 1309151649 );
  a = ii( a, b, c, d, k[ 4 ], 6, -145523070 );
  d = ii( d, a, b, c, k[ 11 ], 10, -1120210379 );
  c = ii( c, d, a, b, k[ 2 ], 15, 718787259 );
  b = ii( b, c, d, a, k[ 9 ], 21, -343485551 );

  x[ 0 ] = add32( a, x[ 0 ] );
  x[ 1 ] = add32( b, x[ 1 ] );
  x[ 2 ] = add32( c, x[ 2 ] );
  x[ 3 ] = add32( d, x[ 3 ] );

}

function cmn( q, a, b, x, s, t ) {
  a = add32( add32( a, q ), add32( x, t ) );
  return add32(( a << s ) | ( a >>> ( 32 - s ) ), b );
}

function ff( a, b, c, d, x, s, t ) {
  return cmn(( b & c ) | ( ( ~b ) & d ), a, b, x, s, t );
}

function gg( a, b, c, d, x, s, t ) {
  return cmn(( b & d ) | ( c & ( ~d ) ), a, b, x, s, t );
}

function hh( a, b, c, d, x, s, t ) {
  return cmn( b ^ c ^ d, a, b, x, s, t );
}

function ii( a, b, c, d, x, s, t ) {
  return cmn( c ^ ( b | ( ~d ) ), a, b, x, s, t );
}

function md51( s ) {
  var txt = '';
  var n = s.length,
      state = [ 1732584193, -271733879, -1732584194, 271733878 ],
      i;
  for( i = 64;i <= s.length;i += 64 ) {
    md5cycle( state, md5blk( s.substring( i - 64, i ) ) );
  }
  s = s.substring( i - 64 );
  var tail = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ];
  for( i = 0;i < s.length;i++ )
    tail[ i >> 2 ] |= s.charCodeAt( i ) << ( ( i % 4 ) << 3 );
  tail[ i >> 2 ] |= 0x80 << ( ( i % 4 ) << 3 );
  if( i > 55 ) {
    md5cycle( state, tail );
    for( i = 0;i < 16;i++ ) tail[ i ] = 0;
  }
  tail[ 14 ] = n * 8;
  md5cycle( state, tail );
  return state;
}

/* there needs to be support for Unicode here,
 * unless we pretend that we can redefine the MD-5
 * algorithm for multi-byte characters (perhaps
 * by adding every four 16-bit characters and
 * shortening the sum to 32 bits). Otherwise
 * I suggest performing MD-5 as if every character
 * was two bytes--e.g., 0040 0025 = @%--but then
 * how will an ordinary MD-5 sum be matched?
 * There is no way to standardize text to something
 * like UTF-8 before transformation; speed cost is
 * utterly prohibitive. The JavaScript standard
 * itself needs to look at this: it should start
 * providing access to strings as preformed UTF-8
 * 8-bit unsigned value arrays.
 */

function md5blk( s ) { /* I figured global was faster.   */
  var md5blks = [],
      i; /* Andy King said do it this way. */
  for( i = 0;i < 64;i += 4 ) {
    md5blks[ i >> 2 ] = s.charCodeAt( i ) + ( s.charCodeAt( i + 1 ) << 8 ) + ( s.charCodeAt( i + 2 ) << 16 ) + ( s.charCodeAt( i + 3 ) << 24 );
  }
  return md5blks;
}

var hex_chr = '0123456789abcdef'.split( '' );

function rhex( n ) {
  var s = '',
      j = 0;
  for( ;j < 4;j++ )
    s += hex_chr[ ( n >> ( j * 8 + 4 ) ) & 0x0F ] + hex_chr[ ( n >> ( j * 8 ) ) & 0x0F ];
  return s;
}

function hex( x ) {
  for( var i = 0;i < x.length;i++ )
    x[ i ] = rhex( x[ i ] );
  return x.join( '' );
}

function md5( s ) {
  return hex( md51( s ) );
}

/* this function is much faster,
 so if possible we use it. Some IEs
 are the only ones I know of that
 need the idiotic second function,
 generated by an if clause.  */

function add32( a, b ) {
  return ( a + b ) & 0xFFFFFFFF;
}

if( md5( 'hello' ) != '5d41402abc4b2a76b9719d911017c592' ) {
  function add32( x, y ) {
    var lsw = ( x & 0xFFFF ) + ( y & 0xFFFF ),
        msw = ( x >> 16 ) + ( y >> 16 ) + ( lsw >> 16 );
    return ( msw << 16 ) | ( lsw & 0xFFFF );
  }
}


( function() {
  function md5cycle( x, k ) {
    var a = x[ 0 ],
        b = x[ 1 ],
        c = x[ 2 ],
        d = x[ 3 ];

    a = ff( a, b, c, d, k[ 0 ], 7, -680876936 );
    d = ff( d, a, b, c, k[ 1 ], 12, -389564586 );
    c = ff( c, d, a, b, k[ 2 ], 17, 606105819 );
    b = ff( b, c, d, a, k[ 3 ], 22, -1044525330 );
    a = ff( a, b, c, d, k[ 4 ], 7, -176418897 );
    d = ff( d, a, b, c, k[ 5 ], 12, 1200080426 );
    c = ff( c, d, a, b, k[ 6 ], 17, -1473231341 );
    b = ff( b, c, d, a, k[ 7 ], 22, -45705983 );
    a = ff( a, b, c, d, k[ 8 ], 7, 1770035416 );
    d = ff( d, a, b, c, k[ 9 ], 12, -1958414417 );
    c = ff( c, d, a, b, k[ 10 ], 17, -42063 );
    b = ff( b, c, d, a, k[ 11 ], 22, -1990404162 );
    a = ff( a, b, c, d, k[ 12 ], 7, 1804603682 );
    d = ff( d, a, b, c, k[ 13 ], 12, -40341101 );
    c = ff( c, d, a, b, k[ 14 ], 17, -1502002290 );
    b = ff( b, c, d, a, k[ 15 ], 22, 1236535329 );

    a = gg( a, b, c, d, k[ 1 ], 5, -165796510 );
    d = gg( d, a, b, c, k[ 6 ], 9, -1069501632 );
    c = gg( c, d, a, b, k[ 11 ], 14, 643717713 );
    b = gg( b, c, d, a, k[ 0 ], 20, -373897302 );
    a = gg( a, b, c, d, k[ 5 ], 5, -701558691 );
    d = gg( d, a, b, c, k[ 10 ], 9, 38016083 );
    c = gg( c, d, a, b, k[ 15 ], 14, -660478335 );
    b = gg( b, c, d, a, k[ 4 ], 20, -405537848 );
    a = gg( a, b, c, d, k[ 9 ], 5, 568446438 );
    d = gg( d, a, b, c, k[ 14 ], 9, -1019803690 );
    c = gg( c, d, a, b, k[ 3 ], 14, -187363961 );
    b = gg( b, c, d, a, k[ 8 ], 20, 1163531501 );
    a = gg( a, b, c, d, k[ 13 ], 5, -1444681467 );
    d = gg( d, a, b, c, k[ 2 ], 9, -51403784 );
    c = gg( c, d, a, b, k[ 7 ], 14, 1735328473 );
    b = gg( b, c, d, a, k[ 12 ], 20, -1926607734 );

    a = hh( a, b, c, d, k[ 5 ], 4, -378558 );
    d = hh( d, a, b, c, k[ 8 ], 11, -2022574463 );
    c = hh( c, d, a, b, k[ 11 ], 16, 1839030562 );
    b = hh( b, c, d, a, k[ 14 ], 23, -35309556 );
    a = hh( a, b, c, d, k[ 1 ], 4, -1530992060 );
    d = hh( d, a, b, c, k[ 4 ], 11, 1272893353 );
    c = hh( c, d, a, b, k[ 7 ], 16, -155497632 );
    b = hh( b, c, d, a, k[ 10 ], 23, -1094730640 );
    a = hh( a, b, c, d, k[ 13 ], 4, 681279174 );
    d = hh( d, a, b, c, k[ 0 ], 11, -358537222 );
    c = hh( c, d, a, b, k[ 3 ], 16, -722521979 );
    b = hh( b, c, d, a, k[ 6 ], 23, 76029189 );
    a = hh( a, b, c, d, k[ 9 ], 4, -640364487 );
    d = hh( d, a, b, c, k[ 12 ], 11, -421815835 );
    c = hh( c, d, a, b, k[ 15 ], 16, 530742520 );
    b = hh( b, c, d, a, k[ 2 ], 23, -995338651 );

    a = ii( a, b, c, d, k[ 0 ], 6, -198630844 );
    d = ii( d, a, b, c, k[ 7 ], 10, 1126891415 );
    c = ii( c, d, a, b, k[ 14 ], 15, -1416354905 );
    b = ii( b, c, d, a, k[ 5 ], 21, -57434055 );
    a = ii( a, b, c, d, k[ 12 ], 6, 1700485571 );
    d = ii( d, a, b, c, k[ 3 ], 10, -1894986606 );
    c = ii( c, d, a, b, k[ 10 ], 15, -1051523 );
    b = ii( b, c, d, a, k[ 1 ], 21, -2054922799 );
    a = ii( a, b, c, d, k[ 8 ], 6, 1873313359 );
    d = ii( d, a, b, c, k[ 15 ], 10, -30611744 );
    c = ii( c, d, a, b, k[ 6 ], 15, -1560198380 );
    b = ii( b, c, d, a, k[ 13 ], 21, 1309151649 );
    a = ii( a, b, c, d, k[ 4 ], 6, -145523070 );
    d = ii( d, a, b, c, k[ 11 ], 10, -1120210379 );
    c = ii( c, d, a, b, k[ 2 ], 15, 718787259 );
    b = ii( b, c, d, a, k[ 9 ], 21, -343485551 );

    x[ 0 ] = add32( a, x[ 0 ] );
    x[ 1 ] = add32( b, x[ 1 ] );
    x[ 2 ] = add32( c, x[ 2 ] );
    x[ 3 ] = add32( d, x[ 3 ] );

  }

  function cmn( q, a, b, x, s, t ) {
    a = add32( add32( a, q ), add32( x, t ) );
    return add32(( a << s ) | ( a >>> ( 32 - s ) ), b );
  }

  function ff( a, b, c, d, x, s, t ) {
    return cmn(( b & c ) | ( ( ~b ) & d ), a, b, x, s, t );
  }

  function gg( a, b, c, d, x, s, t ) {
    return cmn(( b & d ) | ( c & ( ~d ) ), a, b, x, s, t );
  }

  function hh( a, b, c, d, x, s, t ) {
    return cmn( b ^ c ^ d, a, b, x, s, t );
  }

  function ii( a, b, c, d, x, s, t ) {
    return cmn( c ^ ( b | ( ~d ) ), a, b, x, s, t );
  }

  function md51( s ) {
    // Converts the string to UTF-8 "bytes" when necessary
    if( s.match( /[\x80-\xFF]/ ) ) {
      s = unescape( encodeURI( s ) );
    }
    var txt = '';
    var n = s.length,
        state = [ 1732584193, -271733879, -1732584194, 271733878 ],
        i;
    for( i = 64;i <= s.length;i += 64 ) {
      md5cycle( state, md5blk( s.substring( i - 64, i ) ) );
    }
    s = s.substring( i - 64 );
    var tail = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ];
    for( i = 0;i < s.length;i++ )
      tail[ i >> 2 ] |= s.charCodeAt( i ) << ( ( i % 4 ) << 3 );
    tail[ i >> 2 ] |= 0x80 << ( ( i % 4 ) << 3 );
    if( i > 55 ) {
      md5cycle( state, tail );
      for( i = 0;i < 16;i++ ) tail[ i ] = 0;
    }
    tail[ 14 ] = n * 8;
    md5cycle( state, tail );
    return state;
  }

  /* there needs to be support for Unicode here,
   * unless we pretend that we can redefine the MD-5
   * algorithm for multi-byte characters (perhaps
   * by adding every four 16-bit characters and
   * shortening the sum to 32 bits). Otherwise
   * I suggest performing MD-5 as if every character
   * was two bytes--e.g., 0040 0025 = @%--but then
   * how will an ordinary MD-5 sum be matched?
   * There is no way to standardize text to something
   * like UTF-8 before transformation; speed cost is
   * utterly prohibitive. The JavaScript standard
   * itself needs to look at this: it should start
   * providing access to strings as preformed UTF-8
   * 8-bit unsigned value arrays.
   */

  function md5blk( s ) { /* I figured global was faster.   */
    var md5blks = [],
        i; /* Andy King said do it this way. */
    for( i = 0;i < 64;i += 4 ) {
      md5blks[ i >> 2 ] = s.charCodeAt( i ) + ( s.charCodeAt( i + 1 ) << 8 ) + ( s.charCodeAt( i + 2 ) << 16 ) + ( s.charCodeAt( i + 3 ) << 24 );
    }
    return md5blks;
  }

  var hex_chr = '0123456789abcdef'.split( '' );

  function rhex( n ) {
    var s = '',
        j = 0;
    for( ;j < 4;j++ )
      s += hex_chr[ ( n >> ( j * 8 + 4 ) ) & 0x0F ] + hex_chr[ ( n >> ( j * 8 ) ) & 0x0F ];
    return s;
  }

  function hex( x ) {
    for( var i = 0;i < x.length;i++ )
      x[ i ] = rhex( x[ i ] );
    return x.join( '' );
  }

  var md5_utf8 = function( s ) {
    return hex( md51( s ) );
  }

  /* this function is much faster,
   so if possible we use it. Some IEs
   are the only ones I know of that
   need the idiotic second function,
   generated by an if clause.  */

  function add32( a, b ) {
    return ( a + b ) & 0xFFFFFFFF;
  }

  if( md5( 'hello' ) != '5d41402abc4b2a76b9719d911017c592' ) {
    function add32( x, y ) {
      var lsw = ( x & 0xFFFF ) + ( y & 0xFFFF ),
          msw = ( x >> 16 ) + ( y >> 16 ) + ( lsw >> 16 );
      return ( msw << 16 ) | ( lsw & 0xFFFF );
    }
  }
})();

/* md5.js - MD5 Message-Digest
 * Copyright (C) 1999,2002 Masanao Izumo <iz@onicos.co.jp>
 * Version: 2.0.0
 * LastModified: May 13 2002
 *
 * This program is free software.  You can redistribute it and/or modify
 * it without any warranty.  This library calculates the MD5 based on RFC1321.
 * See RFC1321 for more information and algorism.
 */

/* Interface:
 * md5_128bits = MD5_hash(data);
 * md5_hexstr = MD5_hexhash(data);
 */

/* ChangeLog
 * 2002/05/13: Version 2.0.0 released
 * NOTICE: API is changed.
 * 2002/04/15: Bug fix about MD5 length.
 */


//    md5_T[i] = parseInt(Math.abs(Math.sin(i)) * 4294967296.0);
var MD5_T = new Array( 0x00000000, 0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501, 0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821, 0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8, 0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a, 0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70, 0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665, 0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1, 0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391 );

var MD5_round1 = new Array( new Array( 0, 7, 1 ), new Array( 1, 12, 2 ), new Array( 2, 17, 3 ), new Array( 3, 22, 4 ), new Array( 4, 7, 5 ), new Array( 5, 12, 6 ), new Array( 6, 17, 7 ), new Array( 7, 22, 8 ), new Array( 8, 7, 9 ), new Array( 9, 12, 10 ), new Array( 10, 17, 11 ), new Array( 11, 22, 12 ), new Array( 12, 7, 13 ), new Array( 13, 12, 14 ), new Array( 14, 17, 15 ), new Array( 15, 22, 16 ) );

var MD5_round2 = new Array( new Array( 1, 5, 17 ), new Array( 6, 9, 18 ), new Array( 11, 14, 19 ), new Array( 0, 20, 20 ), new Array( 5, 5, 21 ), new Array( 10, 9, 22 ), new Array( 15, 14, 23 ), new Array( 4, 20, 24 ), new Array( 9, 5, 25 ), new Array( 14, 9, 26 ), new Array( 3, 14, 27 ), new Array( 8, 20, 28 ), new Array( 13, 5, 29 ), new Array( 2, 9, 30 ), new Array( 7, 14, 31 ), new Array( 12, 20, 32 ) );

var MD5_round3 = new Array( new Array( 5, 4, 33 ), new Array( 8, 11, 34 ), new Array( 11, 16, 35 ), new Array( 14, 23, 36 ), new Array( 1, 4, 37 ), new Array( 4, 11, 38 ), new Array( 7, 16, 39 ), new Array( 10, 23, 40 ), new Array( 13, 4, 41 ), new Array( 0, 11, 42 ), new Array( 3, 16, 43 ), new Array( 6, 23, 44 ), new Array( 9, 4, 45 ), new Array( 12, 11, 46 ), new Array( 15, 16, 47 ), new Array( 2, 23, 48 ) );

var MD5_round4 = new Array( new Array( 0, 6, 49 ), new Array( 7, 10, 50 ), new Array( 14, 15, 51 ), new Array( 5, 21, 52 ), new Array( 12, 6, 53 ), new Array( 3, 10, 54 ), new Array( 10, 15, 55 ), new Array( 1, 21, 56 ), new Array( 8, 6, 57 ), new Array( 15, 10, 58 ), new Array( 6, 15, 59 ), new Array( 13, 21, 60 ), new Array( 4, 6, 61 ), new Array( 11, 10, 62 ), new Array( 2, 15, 63 ), new Array( 9, 21, 64 ) );

function MD5_F( x, y, z ) {
  return ( x & y ) | ( ~x & z );
}

function MD5_G( x, y, z ) {
  return ( x & z ) | ( y & ~z );
}

function MD5_H( x, y, z ) {
  return x ^ y ^ z;
}

function MD5_I( x, y, z ) {
  return y ^ ( x | ~z );
}

var MD5_round = new Array( new Array( MD5_F, MD5_round1 ), new Array( MD5_G, MD5_round2 ), new Array( MD5_H, MD5_round3 ), new Array( MD5_I, MD5_round4 ) );

function MD5_pack( n32 ) {
  return String.fromCharCode( n32 & 0xff ) + String.fromCharCode(( n32 >>> 8 ) & 0xff ) + String.fromCharCode(( n32 >>> 16 ) & 0xff ) + String.fromCharCode(( n32 >>> 24 ) & 0xff );
}

function MD5_unpack( s4 ) {
  return s4.charCodeAt( 0 ) | ( s4.charCodeAt( 1 ) << 8 ) | ( s4.charCodeAt( 2 ) << 16 ) | ( s4.charCodeAt( 3 ) << 24 );
}

function MD5_number( n ) {
  while( n < 0 )
    n += 4294967296;
  while( n > 4294967295 )
    n -= 4294967296;
  return n;
}

function MD5_apply_round( x, s, f, abcd, r ) {
  var a, b, c, d;
  var kk, ss, ii;
  var t, u;

  a = abcd[ 0 ];
  b = abcd[ 1 ];
  c = abcd[ 2 ];
  d = abcd[ 3 ];
  kk = r[ 0 ];
  ss = r[ 1 ];
  ii = r[ 2 ];

  u = f( s[ b ], s[ c ], s[ d ] );
  t = s[ a ] + u + x[ kk ] + MD5_T[ ii ];
  t = MD5_number( t );
  t = ( ( t << ss ) | ( t >>> ( 32 - ss ) ) );
  t += s[ b ];
  s[ a ] = MD5_number( t );
}

function MD5_hash( data ) {
  var abcd, x, state, s;
  var len, index, padLen, f, r;
  var i, j, k;
  var tmp;

  state = new Array( 0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476 );
  len = data.length;
  index = len & 0x3f;
  padLen = ( index < 56 ) ? ( 56 - index ) : ( 120 - index );
  if( padLen > 0 ) {
    data += "\x80";
    for( i = 0;i < padLen - 1;i++ )
      data += "\x00";
  }
  data += MD5_pack( len * 8 );
  data += MD5_pack( 0 );
  len += padLen + 8;
  abcd = new Array( 0, 1, 2, 3 );
  x = new Array( 16 );
  s = new Array( 4 );

  for( k = 0;k < len;k += 64 ) {
    for( i = 0, j = k;i < 16;i++ , j += 4 ) {
      x[ i ] = data.charCodeAt( j ) | ( data.charCodeAt( j + 1 ) << 8 ) | ( data.charCodeAt( j + 2 ) << 16 ) | ( data.charCodeAt( j + 3 ) << 24 );
    }
    for( i = 0;i < 4;i++ )
      s[ i ] = state[ i ];
    for( i = 0;i < 4;i++ ) {
      f = MD5_round[ i ][ 0 ];
      r = MD5_round[ i ][ 1 ];
      for( j = 0;j < 16;j++ ) {
        MD5_apply_round( x, s, f, abcd, r[ j ] );
        tmp = abcd[ 0 ];
        abcd[ 0 ] = abcd[ 3 ];
        abcd[ 3 ] = abcd[ 2 ];
        abcd[ 2 ] = abcd[ 1 ];
        abcd[ 1 ] = tmp;
      }
    }

    for( i = 0;i < 4;i++ ) {
      state[ i ] += s[ i ];
      state[ i ] = MD5_number( state[ i ] );
    }
  }

  return MD5_pack( state[ 0 ] ) + MD5_pack( state[ 1 ] ) + MD5_pack( state[ 2 ] ) + MD5_pack( state[ 3 ] );
}

function MD5_hexhash( data ) {
  var i, out, c;
  var bit128;

  bit128 = MD5_hash( data );
  out = "";
  for( i = 0;i < 16;i++ ) {
    c = bit128.charCodeAt( i );
    out += "0123456789abcdef".charAt(( c >> 4 ) & 0xf );
    out += "0123456789abcdef".charAt( c & 0xf );
  }
  return out;
}