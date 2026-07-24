'use client';

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { recebivelFormSchema, RecebivelFormValues } from '@/types/recebivel-schema';
import { TipoRecebivel, SimulacaoResultadoDTO } from '@/types/recebivel';
import { recebivelService } from '@/services/recebivel-service';


export function FormularioCadastro() {
  const [simulacao, setSimulacao] = useState<SimulacaoResultadoDTO | null>(null);
  const [carregandoSimulacao, setCarregandoSimulacao] = useState(false);
  const [carregandoLiquidacao, setCarregandoLiquidacao] = useState(false);
  const [mensagemSucesso, setMensagemSucesso] = useState<string | null>(null);
  const [mensagemErro, setMensagemErro] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    getValues,
    formState: { errors },
  } = useForm<RecebivelFormValues>({
    resolver: zodResolver(recebivelFormSchema),
    defaultValues: {
      cedente: '',
      valorOriginal: 0,
      vencimento: '',
      tipo: TipoRecebivel.DUPLICATA,
      moedaOriginal: 'BRL',
    },
  });

  // Dispara a simulação em memória (Sem Lock no Java)
  const lidarComSimulacao = async () => {
    try {
      setCarregandoSimulacao(true);
      setMensagemErro(null);
      
      const valores = getValues();
      const dadosValidados = recebivelFormSchema.parse({
        ...valores,
        valorOriginal: Number(valores.valorOriginal),
      });

      const resultado = await recebivelService.simular(dadosValidados);
      setSimulacao(resultado);
    } catch (err: any) {
      setMensagemErro(err.message || 'Dados inválidos ou erro ao simular.');
    } finally {
      setCarregandoSimulacao(false);
    }
  };

  // Submete o formulário para cadastro e liquidação transacional (Com Lock Pessimista)
  const lidarComSubmissao = async (dados: RecebivelFormValues) => {
    try {
      setCarregandoLiquidacao(true);
      setMensagemErro(null);
      setMensagemSucesso(null);

      await recebivelService.cadastrar(dados);
      
      setMensagemSucesso('Título cadastrado e LIQUIDADO com sucesso no motor transacional!');
      setSimulacao(null); // Limpa o painel de simulação pós-sucesso
    } catch (err: any) {
      setMensagemErro(err.message || 'Erro ao processar liquidação atômica.');
    } finally {
      setCarregandoLiquidacao(false);
    }
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
      {/* Formulário de Operação */}
      <form onSubmit={handleSubmit(lidarComSubmissao)} className="lg:col-span-2 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Cedente / Cliente</label>
            <input
              {...register('cedente')}
              type="text"
              className="w-full rounded-md border border-slate-300 p-2 text-sm focus:border-indigo-500 focus:outline-none"
              placeholder="Nome ou identificador do Cedente"
            />
            {errors.cedente && <p className="text-xs text-red-500 mt-1">{errors.cedente.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Valor Original do Título</label>
            <input
              {...register('valorOriginal', { valueAsNumber: true })}
              type="number"
              step="0.01"
              className="w-full rounded-md border border-slate-300 p-2 text-sm focus:border-indigo-500 focus:outline-none"
              placeholder="0.00"
            />
            {errors.valorOriginal && <p className="text-xs text-red-500 mt-1">{errors.valorOriginal.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Data de Vencimento</label>
            <input
              {...register('vencimento')}
              type="date"
              className="w-full rounded-md border border-slate-300 p-2 text-sm focus:border-indigo-500 focus:outline-none"
            />
            {errors.vencimento && <p className="text-xs text-red-500 mt-1">{errors.vencimento.message}</p>}
          </div>

          <div className="grid grid-cols-2 gap-2">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Tipo de Título</label>
              <select
                {...register('tipo')}
                className="w-full rounded-md border border-slate-300 p-2 text-sm bg-white focus:border-indigo-500 focus:outline-none"
              >
                <option value={TipoRecebivel.DUPLICATA}>Duplicata (Spread 1.5%)</option>
                <option value={TipoRecebivel.CHEQUE}>Cheque (Spread 2.5%)</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Moeda Original</label>
              <select
                {...register('moedaOriginal')}
                className="w-full rounded-md border border-slate-300 p-2 text-sm bg-white focus:border-indigo-500 focus:outline-none"
              >
                <option value="BRL">BRL</option>
                <option value="USD">USD</option>
              </select>
            </div>
          </div>
        </div>

        {/* Alertas de Status do Motor Spring Boot */}
        {mensagemSucesso && <div className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm rounded-md">{mensagemSucesso}</div>}
        {mensagemErro && <div className="p-3 bg-rose-50 border border-rose-200 text-rose-700 text-sm rounded-md">{mensagemErro}</div>}

        {/* Botões de Ação */}
        <div className="flex gap-3 pt-2">
          <button
            type="button"
            onClick={lidarComSimulacao}
            disabled={carregandoSimulacao}
            className="px-4 py-2 text-sm font-medium text-indigo-600 bg-indigo-50 hover:bg-indigo-100 rounded-md transition-colors disabled:opacity-50"
          >
            {carregandoSimulacao ? 'Simulando...' : 'Simular Operação'}
          </button>
          <button
            type="submit"
            disabled={carregandoLiquidacao}
            className="px-4 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-md transition-colors shadow-sm disabled:opacity-50"
          >
            {carregandoLiquidacao ? 'Executando Lock...' : 'Liquidar Título'}
          </button>
        </div>
      </form>

      {/* Painel Lateral Reativo de Exibição */}
      <div className="bg-slate-50 border border-slate-200 rounded-lg p-4 flex flex-col justify-between">
        <div>
          <h3 className="text-sm font-semibold text-slate-900 border-b border-slate-200 pb-2 mb-3">Memória de Cálculo</h3>
          {simulacao ? (
            <div className="space-y-3">
              <div>
                <p className="text-xs text-slate-500 uppercase tracking-wider">Valor Líquido Calculado</p>
                <p className="text-2xl font-bold text-slate-950">
                  {simulacao.moedaLiquidacao} {simulacao.valorLiquido.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </p>
              </div>
              <div className="grid grid-cols-2 gap-2 text-sm pt-2 border-t border-slate-200">
                <div>
                  <p className="text-xs text-slate-500">Spread Cobrado</p>
                  <p className="font-medium text-slate-800">{simulacao.spreadCobrado}% a.m.</p>
                </div>
                <div>
                  <p className="text-xs text-slate-500">Taxa Cambial</p>
                  <p className="font-medium text-slate-800">Fator: {simulacao.taxaAplicada}</p>
                </div>
              </div>
            </div>
          ) : (
            <p className="text-sm text-slate-400 italic">Informe os dados cadastrais do título e execute a simulação para visualizar o cálculo de valor presente e conversão cross-currency.</p>
          )}
        </div>
        <div className="text-[11px] text-slate-400 mt-4 pt-2 border-t border-slate-200">
          Operação integrada ao motor transacional ACID com proteção pessimista.
        </div>
      </div>
    </div>
  );
}
