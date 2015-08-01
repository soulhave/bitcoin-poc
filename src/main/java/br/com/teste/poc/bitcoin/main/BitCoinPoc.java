package br.com.teste.poc.bitcoin.main;

import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ramon on 31/07/2015.
 */
@SuppressWarnings("ALL")
public class BitCoinPoc {

    public static final String SENDREQUEST_EXAMPLE = "sendrequest-example";
    public static final String DESTINO = "2MtckqpP3YyUi1zYptayjZGFycjSKHWdbtA";
    public static final TestNet3Params PARAMETROS = TestNet3Params.get();

    public static void main(String args[]) {
        //Enviar Bit Coins
        //enviarBitCoins();

        //Receber BitCOins
        receberBitCoins();
    }

    // Printa o saldo dos meus bitcoins
    private static void saldoBitCoins(){
        
    }

    //Realizar Recebimento
    private static void receberBitCoins(){
        WalletAppKit kit = getWalletAppKit();
        kit.wallet().addEventListener(new AbstractWalletEventListener() {
            @Override
            public void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
                System.out.println("Recebido: " + tx.getValue(w));
            }
        });

    }

    //Realizar Envio
    public static void enviarBitCoins() {
        WalletAppKit kit = getWalletAppKit();
        System.out.println("Chave para receber:" + kit.wallet().currentReceiveAddress().toString());
        //Quantidade a ser enviada
        Coin valor = Coin.parseCoin("0.09");

        byte[] para = getHash160();

        //Endereço
        Address enviarPara = Address.fromP2SHHash(PARAMETROS, para);

        try {
            Wallet.SendResult resultado = kit.wallet().sendCoins(kit.peerGroup(), enviarPara, valor);
            System.out.println("Coins Enviados:" + resultado.tx.getHashAsString());
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
        }
    }

    // Realiza a conex'ao com a rede
    private static WalletAppKit getWalletAppKit() {
        //Estabelecendo conex'ao com rede de Tests
        WalletAppKit kit = new WalletAppKit(PARAMETROS, new File("."), SENDREQUEST_EXAMPLE);
        kit.startAsync();
        kit.awaitRunning();
        return kit;
    }

    private static byte[] getHash160() {
        MessageDigest digest = null;
        byte[] hash160 = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            hash160 = digest.digest(DESTINO.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash160;
    }
}