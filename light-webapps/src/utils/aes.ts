import CryptoJS from "crypto-js";

const IV = 'e4883f3c48f6e8867265cdbc75b948aa';
const SALT = 'fb1837e8d99840b70d91295965851835';
const KEY_SIZE = 128;
const ITERATION_COUNT = 1000;

function generateKey(passphrase) {
    const salt = CryptoJS.enc.Hex.parse(SALT);
    return CryptoJS.PBKDF2(passphrase, salt, {
        keySize: KEY_SIZE / 32,
        iterations: ITERATION_COUNT,
        hasher: CryptoJS.algo.SHA1
    });
}

export function encrypt(passphrase, plaintext) {
    const key = generateKey(passphrase);
    const iv = CryptoJS.enc.Hex.parse(IV);
    const encrypted = CryptoJS.AES.encrypt(plaintext, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}

export function decrypt(passphrase, ciphertext) {
    const key = generateKey(passphrase);
    const iv = CryptoJS.enc.Hex.parse(IV);
    const decrypted = CryptoJS.AES.decrypt(ciphertext, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return decrypted.toString(CryptoJS.enc.Utf8);
}
