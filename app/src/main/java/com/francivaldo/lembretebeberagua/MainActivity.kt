package com.francivaldo.lembretebeberagua

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.francivaldo.lembretebeberagua.model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var edt_peso:EditText
    private lateinit var edt_idade:EditText
    private lateinit var bt_calcular:Button
    private lateinit var txt_resultado_ml:TextView
    private lateinit var ic_redefinir_dados:ImageView
    private lateinit var bt_lembrete:Button
    private lateinit var bt_alarme:Button
    private lateinit var txt_hora:TextView
    private lateinit var txt_minutos:TextView
    private lateinit var calcularIngestaoDiaria: CalcularIngestaoDiaria
    private var resultadoMl = 0.0
    lateinit var timePickerdialog:TimePickerDialog
    lateinit var calendario:Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        IniciarComponentes()

        calcularIngestaoDiaria = CalcularIngestaoDiaria()

        bt_calcular.setOnClickListener {
            if (edt_peso.text.toString().isEmpty()){
                Toast.makeText(this,"informe seu peso",Toast.LENGTH_SHORT).show();
            }else if(edt_idade.text.toString().isEmpty()){
                Toast.makeText(this,"informe sua idade",Toast.LENGTH_SHORT).show();
            }else{
                var peso = edt_peso.text.toString().toDouble()
                var idade = edt_idade.text.toString().toInt()
                calcularIngestaoDiaria.CalcularTotal(peso,idade)
                resultadoMl = calcularIngestaoDiaria.ResultadoML()
                var formatar = NumberFormat.getNumberInstance(Locale("pt","BR"))
                formatar.isGroupingUsed = false
                txt_resultado_ml.text = formatar.format(resultadoMl) + " "+"ml"
            }
        }
        ic_redefinir_dados.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Redefinir Dados")
                .setMessage("deseja excluir todos os dados existentes?")
                .setPositiveButton("OK",{dialogInterfade,i ->
                    edt_peso.setText("")
                    edt_idade.setText("")
                    txt_resultado_ml.text = ""
                })
            alertDialog.setNegativeButton("Cancelar",{dialogInterfade,i ->

            })
            val dialog = alertDialog.create()
            dialog.show()
        }
        bt_lembrete.setOnClickListener {
            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerdialog = TimePickerDialog(this,{timePicker:TimePicker,hourOfDay:Int,minutes:Int ->
                txt_hora.text = String.format("%02d",hourOfDay)
                txt_minutos.text = String.format("%02d",minutes)
            },horaAtual,minutosAtuais,true)
            timePickerdialog.show()
        }
        bt_alarme.setOnClickListener {
            if(!txt_hora.text.toString().isEmpty() && !txt_minutos.text.toString().isEmpty()){
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR,txt_hora.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES,txt_minutos.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE,"hora de beber agua!")
                startActivity(intent)
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
            }
        }
    }
    private fun IniciarComponentes(){
        edt_peso = findViewById(R.id.edt_peso)
        edt_idade = findViewById(R.id.edt_idade)
        bt_calcular = findViewById(R.id.bt_calcular)
        txt_resultado_ml = findViewById(R.id.txt_resultado_ml)
        ic_redefinir_dados = findViewById(R.id.ic_redefinir)
        bt_lembrete = findViewById(R.id.bt_definir_lembrete)
        bt_alarme  = findViewById(R.id.bt_alarme)
        txt_hora = findViewById(R.id.txt_hora)
        txt_minutos = findViewById(R.id.txt_minutos)
    }
}