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

  const lidarComSubmissao = async (dados: RecebivelFormValues) => {
    try {
      setCarregandoLiquidacao(true);
      setMensagemErro(null);
      setMensagemSucesso(null);

      await recebivelService.cadastrar(dados);
      
      setMensagemSucesso('Título cadastrado e LIQUIDADO com sucesso no motor transacional!');
      setSimulacao(null);
    } catch (err: any) {
      setMensagemErro(err.message || 'Erro ao processar liquidação atômica.');
    } finally {
      setCarregandoLiquidacao(false);
    }
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <form onSubmit={handleSubmit(lidarComSubmissao)} className="lg:col-span-2 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
              Cedente / Cliente
            </label>
            <input
              {...register('cedente')}
              type="text"
              className="w-full rounded-md border border-slate-300 p-2 text-sm focus:border-indigo-500 focus:outline-none"
              placeholder="Nome ou identificador do Cedente"
            />
            {errors.cedente && <p className="text-xs text-red-500 mt-1">{errors.cedente.message}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Valor Original do Título</label>
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
            <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Data de Vencimento</label>
            <input
              {...register('vencimento')}
              type="date"
              className="w-full rounded-md border border-slate-300 p-2 text-sm focus:border-indigo-500 focus:outline-none"
            />
            {errors.vencimento && <p className="text-xs text-red-500 mt-1">{errors.vencimento.message}</p>}
          </div>

          <div className="grid grid-cols-2 gap-2">
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Tipo de Título</label>
              <select
                {...register('tipo')}
                className="w-full rounded-md border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-900 p-2 text-sm text-slate-900 dark:text-slate-100 focus:border-indigo-500 focus:outline-none transition-colors"
              >
                <option value={TipoRecebivel.DUPLICATA} className="bg-white dark:bg-slate-900 text-slate-900 dark:text-slate-100">
                  Duplicata (Spread 1.5%)
                </option>
                <option value={TipoRecebivel.CHEQUE} className="bg-white dark:bg-slate-900 text-slate-900 dark:text-slate-100">
                  Cheque (Spread 2.5%)
                </option>
              </select>
            </div>
           <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">Moeda Original</label>
              <select
                {...register('moedaOriginal')}
                className="w-full rounded-md border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-900 p-2 text-sm text-slate-900 dark:text-slate-100 focus:border-indigo-500 focus:outline-none transition-colors"
              >
                <option value="BRL" className="bg-white dark:bg-slate-900 text-slate-900 dark:text-slate-100">BRL</option>
                <option value="USD" className="bg-white dark:bg-slate-900 text-slate-900 dark:text-slate-100">USD</option>
              </select>
            </div>
          </div>
        </div>

        {mensagemSucesso && <div className="p-3 bg-emerald-50 border border-emerald-200 text-emerald-700 text-sm rounded-md">{mensagemSucesso}</div>}
        {mensagemErro && <div className="p-3 bg-rose-50 border border-rose-200 text-rose-700 text-sm rounded-md">{mensagemErro}</div>}

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
    </div>
  );
}
