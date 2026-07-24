'use client';

import { useState } from 'react';
import { useForm as useReactHookForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { taxaFormSchema, TaxaFormValues } from '../../types/taxa-schema';
import { recebivelService } from '../../services/recebivel-service';
import Link from 'next/link';

export default function TaxasPage() {
  const [carregando, setCarregando] = useState(false);
  const [mensagemSucesso, setMensagemSucesso] = useState<string | null>(null);
  const [mensagemErro, setMensagemErro] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useReactHookForm<TaxaFormValues>({
    resolver: zodResolver(taxaFormSchema),
    defaultValues: {
      moedaOrigem: '',
      moedaDestino: '',
      fatorConversao: 1,
    },
  });

  const submeterTaxa = async (dados: TaxaFormValues) => {
    try {
      setCarregando(true);
      setMensagemErro(null);
      setMensagemSucesso(null);

      await recebivelService.salvarTaxa(dados);

      setMensagemSucesso(`Fator ${dados.moedaOrigem}/${dados.moedaDestino} persistido com sucesso!`);
      reset(); 
    } catch (err: any) {
      setMensagemErro(err.message || 'Erro ao comunicar com o motor de câmbio.');
    } finally {
      setCarregando(false);
    }
  };

  return (
    <main className="container mx-auto p-6 max-w-2xl space-y-6 min-h-screen flex flex-col justify-center">
      <div className="flex justify-start">
        <Link 
          href="/operador" 
          className="text-sm font-medium text-indigo-600 dark:text-indigo-400 hover:underline transition-all"
        >
          ← Voltar para o Painel do Operador
        </Link>
      </div>

      <div className="border-b border-slate-200 dark:border-slate-800 pb-4">
        <h1 className="text-3xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
          Gerenciador de Câmbio
        </h1>
        <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
          Cadastre ou atualize multiplicadores de alta precisão na tabela de taxas.
        </p>
      </div>

      <section className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm p-6">
        <form onSubmit={handleSubmit(submeterTaxa)} className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Moeda Origem
              </label>
              <input
                {...register('moedaOrigem')}
                type="text"
                placeholder="USD"
                maxLength={3}
                className="w-full rounded-md border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 p-2 text-sm text-slate-900 dark:text-slate-100 focus:border-indigo-500 focus:outline-none transition-colors"
              />
              {errors.moedaOrigem && <p className="text-xs text-red-500 mt-1">{errors.moedaOrigem.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Moeda Destino
              </label>
              <input
                {...register('moedaDestino')}
                type="text"
                placeholder="BRL"
                maxLength={3}
                className="w-full rounded-md border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 p-2 text-sm text-slate-900 dark:text-slate-100 focus:border-indigo-500 focus:outline-none transition-colors"
              />
              {errors.moedaDestino && <p className="text-xs text-red-500 mt-1">{errors.moedaDestino.message}</p>}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
              Fator de Conversão (Câmbio)
            </label>
            <input
              {...register('fatorConversao', { valueAsNumber: true })}
              type="number"
              step="0.000001"
              placeholder="5.250000"
              className="w-full rounded-md border border-slate-300 dark:border-slate-700 bg-white dark:bg-slate-800 p-2 text-sm text-slate-900 dark:text-slate-100 focus:border-indigo-500 focus:outline-none transition-colors"
            />
            {errors.fatorConversao && <p className="text-xs text-red-500 mt-1">{errors.fatorConversao.message}</p>}
          </div>

          {mensagemSucesso && <div className="p-3 bg-emerald-50 dark:bg-emerald-950/30 border border-emerald-200 dark:border-emerald-800 text-emerald-700 dark:text-emerald-400 text-sm rounded-md">{mensagemSucesso}</div>}
          {mensagemErro && <div className="p-3 bg-rose-50 dark:bg-rose-950/30 border border-rose-200 dark:border-rose-800 text-rose-700 dark:text-rose-400 text-sm rounded-md">{mensagemErro}</div>}

          <button
            type="submit"
            disabled={carregando}
            className="w-full px-4 py-2 text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 dark:bg-indigo-500 dark:hover:bg-indigo-600 rounded-md transition-colors shadow-sm disabled:opacity-50"
          >
            {carregando ? 'Persistindo no Banco...' : 'Atualizar Taxa Cambial'}
          </button>
        </form>
      </section>
    </main>
  );
}
